# Simulator

## Arguments

| Key | Value | Default | Description |
|-----|-------|---------|-------------|
|sleep| integer | `5000` | Timeout between each location update
| real-life | integer | `30` | Each location update is 30 seconds in real life
| trips | integer | `10` | Maximum of 10 trips for a car tracker. This value is random
| carTrackers | Array<String> | `[]` | The array fo cartracker ids, comma separated
| endpoint | string | `https://api.ersols.online/v1` | The base url for the web requests. `/trips` and `/trips/{id}/finished` path

Supply the arguments with `--key=value`.

### Example
```
$ java -jar simulator.jar --sleep=0 --carTrackers=a,b,c
```

## Requirements

Local MongoDB instance for loading the routes. This can be changed with the argument `spring.data.mongodb.host`