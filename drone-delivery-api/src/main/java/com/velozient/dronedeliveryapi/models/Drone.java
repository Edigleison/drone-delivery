package com.velozient.dronedeliveryapi.models;

import java.util.*;

public class Drone {
    private String name;
    private Long maximumWeight;
    private Map<Long, List<Delivery>> trips;

    private List<Delivery> openTrip;
    private long spaceOnOpenTrip;

    public Drone(String name, Long maximumWeight) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid drone name");
        }

        if (maximumWeight == null || maximumWeight <= 0) {
            throw new IllegalArgumentException("Invalid drone maximum weight");
        }

        this.name = name;
        this.maximumWeight = maximumWeight;
        trips = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Long getMaximumWeight() {
        return maximumWeight;
    }

    public long getClosedTripsLength() {
        return trips.size();
    }

    public Map<Long, List<Delivery>> getTrips() {
        return Collections.unmodifiableMap(trips);
    }

    private void newTrip(Delivery delivery) {
        if (delivery.getPackageWeight() > maximumWeight) {
            throw new IllegalArgumentException("Delivery weighing more than the drone capacity");
        }

        if (openTrip != null) {
            closeOpenTrip();
        }

        openTrip = new ArrayList<>();
        openTrip.add(delivery);
        spaceOnOpenTrip = maximumWeight - delivery.getPackageWeight();
        if (spaceOnOpenTrip == 0L) {
            closeOpenTrip();
        }
    }

    public void closeOpenTrip() {
        if (openTrip != null) {
            trips.put(trips.size() + 1L, openTrip);
            openTrip = null;
            spaceOnOpenTrip = 0L;
        }
    }

    public void addTrip(Delivery delivery) {
        if (spaceOnOpenTrip >= delivery.getPackageWeight()) {
            openTrip.add(delivery);
            spaceOnOpenTrip = spaceOnOpenTrip - delivery.getPackageWeight();
            if (spaceOnOpenTrip == 0L) {
                closeOpenTrip();
            }
        } else {
            closeOpenTrip();
            newTrip(delivery);
        }
    }

    public long getSpaceOnOpenTrip() {
        return spaceOnOpenTrip;
    }

    public boolean hasOpenTrip() {
        return openTrip != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Drone drone = (Drone) o;

        if (!name.equals(drone.name)) return false;
        return maximumWeight.equals(drone.maximumWeight);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + maximumWeight.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "[" + name + ']';
    }
}
