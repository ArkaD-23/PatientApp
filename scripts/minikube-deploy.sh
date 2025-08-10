#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
NAMESPACE=patient-app

if ! minikube status >/dev/null 2>&1; then
  echo "Starting minikube..."
  minikube start --driver=docker
fi

echo "Ensuring istioctl installed or not....."
if ! command -v istioctl >/dev/null 2>&1; then
  echo "Please install istioctl and add to PATH; aborting."
  exit 1
fi

echo "Installing istio....."
istioctl install --set profile=demo -y || true

kubectl apply -f "${ROOT_DIR}/k8s/namespace.yaml"

echo "Building docker images....."
eval $(minikube docker-env)
docker build -t user-service:latest "${ROOT_DIR}/user_service"
docker build -t auth-service:latest "${ROOT_DIR}/auth_service"
docker build -t api-gateway:latest "${ROOT_DIR}/api_gateway"
eval $(minikube docker-env -u)

echo "Applying k8s manifests....."
kubectl apply -f "${ROOT_DIR}/k8s/user/service.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/user/deployment.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/auth/service.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/auth/deployment.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/gateway/service.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/gateway/deployment.yaml"

echo "Istio resources....."
kubectl apply -f "${ROOT_DIR}/k8s/istio/gateway.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/istio/virtualservice.yaml"
kubectl apply -f "${ROOT_DIR}/k8s/istio/destinationrule.yaml"

echo "Waiting for pods to get ready....."
kubectl -n ${NAMESPACE} wait --for=condition=ready pod -l app=user-service --timeout=120s || true
kubectl -n ${NAMESPACE} wait --for=condition=ready pod -l app=auth-service --timeout=120s || true
kubectl -n ${NAMESPACE} wait --for=condition=ready pod -l app=api-gateway --timeout=120s || true

echo "Show ingress url"
minikube service istio-ingressgateway -n istio-system --url
echo "Done. Test via the Ingress URL."
