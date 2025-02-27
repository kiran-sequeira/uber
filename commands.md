# Redis Geohashing Commands Reference

## Basic Operations

### Add Locations
```redis
# Single location
GEOADD drivers:geo <longitude> <latitude> <driverId>

# Multiple locations
GEOADD drivers:geo <longitude1> <latitude1> <driverId1> <longitude2> <latitude2> <driverId2>
```

### View Locations
```redis
# View coordinates of members
GEOPOS drivers:geo <driverId1> [driverId2 ...]

# List all members
ZRANGE drivers:geo 0 -1

# List with geohash scores
ZRANGE drivers:geo 0 -1 WITHSCORES
```

### Delete Locations
```redis
# Remove single member
ZREM drivers:geo <driverId>

# Remove multiple members
ZREM drivers:geo <driverId1> <driverId2>

# Delete entire geo index
DEL drivers:geo
```

## Search Operations

### Radius Search
```redis
# Search by coordinates
GEOSEARCH drivers:geo FROMLONLAT <longitude> <latitude> BYRADIUS <distance> <unit>

# Search from member
GEOSEARCH drivers:geo FROMMEMBER <driverId> BYRADIUS <distance> <unit>

# With coordinates and distance
GEOSEARCH drivers:geo FROMLONLAT <longitude> <latitude> BYRADIUS <distance> <unit> WITHCOORD WITHDIST

# Sorted by distance (nearest first)
GEOSEARCH drivers:geo FROMLONLAT <longitude> <latitude> BYRADIUS <distance> <unit> ASC
```

## Distance Calculations

### Get Distance
```redis
# Default (meters)
GEODIST drivers:geo <driverId1> <driverId2>

# Specific unit
GEODIST drivers:geo <driverId1> <driverId2> [m|km|mi|ft]
```

## Utility Commands

### Key Management
```redis
# List all keys
KEYS *

# Get key type
TYPE drivers:geo

# Member count
ZCARD drivers:geo

# Check member exists
ZSCORE drivers:geo <driverId>
```

## Example Usage

### Adding Driver
```redis
GEOADD drivers:geo 72.8777 19.0760 "driver123"
```

### Finding Nearby Drivers
```redis
GEOSEARCH drivers:geo FROMLONLAT 72.8777 19.0760 BYRADIUS 5 km WITHCOORD WITHDIST ASC
```

### Removing Driver
```redis
ZREM drivers:geo "driver123"
```

## Notes
- Available distance units: m (meters), km (kilometers), mi (miles), ft (feet)
- All distances default to meters if unit not specified
- GEOSEARCH returns results in unsorted order unless ASC/DESC specified
- Coordinates must be valid longitude (-180 to 180) and latitude (-85.05 to 85.05)

## Understanding Redis Geohash Storage

### Internal Storage Structure
Redis stores geospatial data as a sorted set (zset):
```
drivers:geo (key)
  └── Sorted Set (zset)
      ├── member1 (driverId) → geohash score1
      ├── member2 (driverId) → geohash score2
      └── member3 (driverId) → geohash score3
```

### Viewing Raw Storage
```redis
# View members with their geohash scores (internal representation)
ZRANGE drivers:geo 0 -1 WITHSCORES

# Example output:
1) "driver123"
2) "3479099956230698"    # This is the geohash score
3) "driver456"
4) "3479099956230799"
```

### Viewing Decoded Coordinates
```redis
# View actual coordinates for members
GEOPOS drivers:geo driver123 driver456

# Example output:
1) 1) "72.87770092487335205"   # longitude
   2) "19.07600025909535366"   # latitude
2) 1) "72.87780092487335205"
   2) "19.07700025909535366"
```

### Debug Commands
```redis
# Check storage type
TYPE drivers:geo
# Returns "zset"

# View complete internal structure
ZRANGE drivers:geo 0 -1 WITHSCORES WITHCOORD

# Check memory usage
MEMORY USAGE drivers:geo
```
