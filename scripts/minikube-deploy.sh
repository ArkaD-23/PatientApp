#!/usr/bin/env bash
set -euo pipefail

NAMESPACE=patient-app
USER_IMG=user-service:latest
AUTH_IMG=auth-service:latest

echo "1) Start minikube (if not running)"
minikube status >/dev/null 2>&1 || minikube start --driver=docker

echo "2) Install Istio (if not installed)"
if ! istioctl version >/dev/null 2>&1; then
  echo "Install Istio CLI or add it to PATH first."
fi
# install istio in demo profile (idempotent)
istioctl install --set profile=demo -y

echo "3) Create namespace with sidecar injection"
kubectl apply -f ../k8s/namespace.yaml

echo "4) Build docker images inside minikube's docker daemon"
eval $(minikube docker-env)
echo "Building user image..."
docker build -t ${USER_IMG} ../user_service
echo "Building auth image..."
docker build -t ${AUTH_IMG} ../auth_service
# return to host docker env
eval $(minikube docker-env -u)

echo "5) Deploy services"
kubectl apply -f ../k8s/user/service.yaml
kubectl apply -f ../k8s/user/deployment.yaml
kubectl apply -f ../k8s/auth/service.yaml
kubectl apply -f ../k8s/auth/deployment.yaml

echo "6) Deploy Istio gateway & virtual service"
kubectl apply -f ../k8s/istio/gateway.yaml
kubectl apply -f ../k8s/istio/virtualservice.yaml
kubectl apply -f ../k8s/istio/destinationrule.yaml

echo "7) Wait for pods ready"
kubectl -n ${NAMESPACE} wait --for=condition=ready pod -l app=user-service --timeout=120s || true
kubectl -n ${NAMESPACE} wait --for=condition=ready pod -l app=auth-service --timeout=120s || true

echo "8) Get ingress IP/port"
INGRESS_IP=$(minikube ip)
INGRESS_PORT=80
echo "Gateway exposed at http://${INGRESS_IP}:${INGRESS_PORT}"

echo "Done. To test, run commands in README or use curl/postman."
