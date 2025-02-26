# Redis Geohashing Commands Reference

[Previous sections remain the same...]

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

### How Geohashing Works
- Redis converts longitude and latitude into a 52-bit geohash
- The geohash is stored as the score in the sorted set
- Nearby locations have similar geohash values
- The driverId is stored as the member name
- This structure enables efficient:
  - Storage of coordinates
  - Proximity searches
  - Radius queries
  - Spatial relationship maintenance

[Rest of the document remains the same...]