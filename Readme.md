

## Setup Flink Cluster

```bash
docker network create app_network
docker run --net=app_network --name jobmanager -p 8081:8081 -e "JOB_MANAGER_RPC_ADDRESS=jobmanager" --expose 6123 -d -t flink:1.9 jobmanager
docker run --net=app_network --name taskmanager1 -e "JOB_MANAGER_RPC_ADDRESS=jobmanager" -d -t flink:1.9 taskmanager

```

## Tear Down Flink Cluster
```bash
docker rm -f flink_jobmanager_1.9 flink_taskmanager_1_1.9
```