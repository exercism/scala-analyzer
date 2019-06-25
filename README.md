# scala-analyzer

The Scala track project-auto-mentor analyzer using [Scalameta](https://scalameta.org/)


## Running in docker

To analyze a solution in docker,
1. Build the image
```
docker build -t exercism/scala-analyzer .
```
2. Run the image
```
docker run -v <~/a/local/solution>:/solution exercism/scala-analyzer <slug> /solution
```