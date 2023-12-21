# Interactive Queries

Kafka Streams Interactive Queries implementation examples.

## Multi K8s Cluster with North-South Communication (Ingress)

### Prerequisites

For this implementation you will need:

- One Kafka Cluster
- Two Kubernetes Cluster
- [Kubernetes Ingress Controller solution](https://kubernetes.io/docs/concepts/services-networking/ingress-controllers/)

### Solution Diagram
  
![north-south](https://github.com/ogomezso/kstreams-playground/blob/main/assets/north-south.png)

### Query Execution example

1. **Query http Request:** Client perform a http request to _Interactive Queries Server_ exposed via Load Balancer
2. **Redirection to 1 cluster:** Load Balancer redirects request to one of the exposed ingress controllers.
3. **Ingress controller redirection:** Ingress Controller redirect to one instance service.
4. **Interactive Queries Rest Server receives the request:** The instance _Interactive Queries Rest Server_ (configured with the local K8s DNS) receive the request and determine if the it can serve the response. F-rom now we gonna describe the _worst case scenario_  in which the request can not be served by the instance and after hitting the others the one that can do it's located on the other cluster (we avoid the sequence of hitting all the instances).
5. (to 7) **Kafka Streams Instance call other Instances Rest Servers:** After respond a 404 (not found) the Kafka Streams instance starts to call to the other instances Rest Server using the **Application Server Configuration** (set up with the ingress DNS url for the specific service instance) until it gets a 200 response or all of them respond with 4xx.
8. (to 15) **Response propagation to the client:** After obtaining the desired response (hopefully 200 ok) the response needs to be propagated through all the way back to the client.

## Multi K8s Cluster with East-West Communication (To be Implemented)

### Prerequisites

For this implementation you will need:

- One Kafka Cluster
- Two Kubernetes Cluster
- Implement a [Kubernetes Multicluster Communications solution](https://www.cncf.io/blog/2021/04/12/simplifying-multi-clusters-in-kubernetes/)

### Solution Diagram
  
![north-south](https://github.com/ogomezso/kstreams-playground/blob/main/assets/east-west.png)

### Query Execution example

1. **Query http Request:** Client perform a http request to _Interactive Queries Server_ exposed via Load Balancer
2. **Redirection to 1 cluster:** Load Balancer redirects request to one of the exposed headless services.
3. **Headless controller redirection:** Ingress Controller redirect to one instance service.
4. **Interactive Queries Rest Server receives the request:** The instance _Interactive Queries Rest Server_ (configured with the local K8s DNS) receive the request and determine if the it can serve the response. From now we gonna describe the _worst case scenario_  in which the request can not be served by the instance and after hitting the others the one that can do it's located on the other cluster (we avoid the sequence of hitting all the instances).
5. (to 7) **Kafka Streams Instance call other Instances Rest Servers:** After respond a 404 (not found) the Kafka Streams instance starts to call to the other instances Rest Server using the **Application Server Configuration** (set up with the opposite cluster url for the specific service instance) until it gets a 200 response or all of them respond with 4xx.
8. (to 15) **Response propagation to the client:** After obtaining the desired response (hopefully 200 ok) the response needs to be propagated through all the way back to the client.

## Multi K8s Cluster Observer Pattern (To be Implemented)

In this case Wordcount services don't expose any interactive query just output the results of their aggregations to the wordcount output topics, observers put these result on a global Ktable (to be able to scale observers) and expose the interactive queries server the same as in example 1. **Important to note that in this case observers are completely different kafka streams applications with their own app id**.

### Prerequisites

For this implementation you will need:

- One Kafka Cluster
- Two Kubernetes Cluster
- Any way to expose the observer service to external request (Ingress Controller preferred)

### Solution Diagram
  
![north-south](https://github.com/ogomezso/kstreams-playground/blob/main/assets/observer.png)

### Query Execution example

1. **Query http Request:** Client perform a http request to _Interactive Queries Server_ exposed via Load Balancer
2. **Redirection to 1 cluster:** Load Balancer redirects request to one of the exposed observer services
3. **Headless controller redirection / Interactive Queries Rest Server receives the request:** As _Observer services_ contains the entire state on the global state store any instance that catch the request will be able to bring a 2xx response.
4. (to 6) **Response propagation to the client:** After obtaining the desired response (hopefully 200 ok) the response needs to be propagated through all the way back to the client.
