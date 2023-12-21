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
5. (to 7) **Kafka Streams Instance call other Instances Rest Servers:** After respond a 404 (not found) the Kafka Streams intance starts to call to the other instances Rest Server using the **Application Server Configuration** (set up with the ingress DNS url for the specific service instance) until it gets a 200 response or all of them respond with 4xx.
8. (to 15) **Response propagation to the client:** After obtaining the desired response (hopefully 200 ok) the response needs to be propagated through all the way back to the client.
