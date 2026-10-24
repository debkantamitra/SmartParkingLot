package com.airtribe.smartparkinglot.service;

import com.airtribe.smartparkinglot.entity.ParkingSpot;
import com.airtribe.smartparkinglot.entity.ParkingTicket;
import com.airtribe.smartparkinglot.entity.ParkingReceipt;
import com.airtribe.smartparkinglot.entity.Vehicle;
import com.airtribe.smartparkinglot.entity.SpotType;
import com.airtribe.smartparkinglot.exception.NoSpotAvailableException;
import com.airtribe.smartparkinglot.exception.ParkingTransactionException;
import com.airtribe.smartparkinglot.repository.Repository;
import com.airtribe.smartparkinglot.service.allocation.SpotAllocationStrategy;
import com.airtribe.smartparkinglot.service.fee.FeeCalculationStrategy;
import com.airtribe.smartparkinglot.service.fee.FeeStrategyFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLotService {
    private final Repository<ParkingSpot> spotRepository;
    private final Repository<ParkingTicket> ticketRepository;
    private final SpotAllocationStrategy allocationStrategy;
    private final Clock clock;
    private final AtomicLong ticketSequence = new AtomicLong();
    private final ReentrantLock transactionLock = new ReentrantLock(true);

    public ParkingLotService(
            Repository<ParkingSpot> spotRepository,
            Repository<ParkingTicket> ticketRepository,
            SpotAllocationStrategy allocationStrategy,
            Clock clock
    ) {
        this.spotRepository = spotRepository;
        this.ticketRepository = ticketRepository;
        this.allocationStrategy = allocationStrategy;
        this.clock = clock;
    }

    public ParkingTicket checkIn(Vehicle vehicle) {
        transactionLock.lock();
        try {
            boolean alreadyParked = ticketRepository.findAll().stream()
                    .anyMatch(ticket -> ticket.vehicle().getLicensePlate()
                            .equals(vehicle.getLicensePlate()));

            if (alreadyParked) {
                throw new ParkingTransactionException(
                        "Vehicle is already parked: " + vehicle.getLicensePlate());
            }

            ParkingSpot spot = allocationStrategy.findSpot(vehicle, spotRepository.findAll())
                    .orElseThrow(() -> new NoSpotAvailableException(
                            "No suitable spot available for " + vehicle.getType()));

            spot.occupy();

            try {
                String ticketId = "T-%06d".formatted(ticketSequence.incrementAndGet());
                ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot.getId(), clock.instant());
                ticketRepository.add(ticketId, ticket);

                return ticket;
            } catch (RuntimeException exception) {
                spot.release();
                throw exception;
            }
        } finally {
            transactionLock.unlock();
        }
    }

    public ParkingReceipt checkOut(String ticketId) {
        transactionLock.lock();
        try {
            ParkingTicket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new ParkingTransactionException(
                            "Active ticket not found: " + ticketId));

            ParkingSpot spot = spotRepository.findById(ticket.spotId())
                    .orElseThrow(() -> new ParkingTransactionException(
                            "Parking spot not found: " + ticket.spotId()));

            Instant exitTime = clock.instant();
            Duration duration = Duration.between(ticket.entryTime(), exitTime);
            FeeCalculationStrategy feeStrategy = FeeStrategyFactory.forVehicle(ticket.vehicle().getType());
            ParkingReceipt receipt = new ParkingReceipt(
                    ticket.id(),
                    ticket.vehicle().getLicensePlate(),
                    spot.getId(),
                    ticket.entryTime(),
                    exitTime,
                    duration,
                    feeStrategy.calculate(duration)
            );

            spot.release();
            ticketRepository.remove(ticketId);
            return receipt;
        } finally {
            transactionLock.unlock();
        }
    }

    public Map<SpotType, Long> availableSpots() {
        transactionLock.lock();
        try {
            Map<SpotType, Long> availability = new EnumMap<>(SpotType.class);
            for (SpotType type : SpotType.values()) {
                availability.put(type, 0L);
            }

            spotRepository.findAll().stream()
                    .filter(spot -> !spot.isOccupied())
                    .forEach(spot -> availability.merge(spot.getType(), 1L, Long::sum));

            return Map.copyOf(availability);
        } finally {
            transactionLock.unlock();
        }
    }
}
