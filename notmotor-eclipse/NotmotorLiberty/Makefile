# Makefile for base-template-shift project
# Works on Linux/macOS with make installed
# For Windows: use build.bat or build.ps1 scripts, or install make via Git Bash/Chocolatey/WSL

.PHONY: help build package clean dev devc start stop status test docker-build docker-run compose-up compose-down compose-dev-up compose-dev-down

# Default target
help: ## Show this help message
	@echo "Available targets:"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-20s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# Maven targets
clean: ## Clean the project
	./mvnw clean

compile: ## Compile the project
	./mvnw compile

package: ## Package the application (WAR file)
	./mvnw package

test: ## Run tests
	./mvnw test

# Liberty targets
dev: ## Start Liberty in development mode (local)
	./mvnw liberty:dev

devc: ## Start Liberty in development mode (containerized)
	./mvnw liberty:devc

runc: ## Run Liberty in container mode
	./mvnw liberty:runc

start: ## Start Liberty server
	./mvnw liberty:start

stop: ## Stop Liberty server
	./mvnw liberty:stop

status: ## Check Liberty server status
	./mvnw liberty:status

# Container targets
docker-build: package ## Build Docker/Podman image
	podman build -t base-template-shift:latest .

docker-run: ## Run the container manually
	podman run --name base-template-shift \
		--network csn-dev-net \
		-e OTEL_EXPORTER_OTLP_ENDPOINT=http://otel:4317 \
		-p 9080:9080 \
		-ti base-template-shift:latest

docker-clean: ## Remove existing container
	podman rm -f base-template-shift || true

# Network setup
network-create: ## Create the csn-dev-net network
	podman network create csn-dev-net || true

network-inspect: ## Inspect the csn-dev-net network
	podman network inspect csn-dev-net

# Docker Compose targets
compose-up: docker-build ## Start with Docker Compose (production)
	podman-compose up -d

compose-down: ## Stop Docker Compose stack
	podman-compose down

compose-logs: ## Show logs from compose stack
	podman-compose logs -f

compose-dev-up: ## Start with Docker Compose (development with auto-build)
	podman-compose -f docker-compose.dev.yml up -d --build

compose-dev-down: ## Stop development Docker Compose stack
	podman-compose -f docker-compose.dev.yml down

compose-dev-logs: ## Show logs from development compose stack
	podman-compose -f docker-compose.dev.yml logs -f

# OTEL targets
otel-start: network-create ## Start OTEL-LGTM stack
	podman run -d --name otel \
		--network csn-dev-net \
		-p 3000:3000 -p 4317:4317 -p 4318:4318 \
		grafana/otel-lgtm:latest

otel-stop: ## Stop OTEL-LGTM stack
	podman rm -f otel || true

otel-logs: ## Show OTEL logs
	podman logs -f otel

# Testing targets
test-api: ## Test the REST API endpoints
	@echo "Testing GET endpoint:"
	curl -s "http://localhost:9080/base-template-shift/api/hello" | jq . || curl -s "http://localhost:9080/base-template-shift/api/hello"
	@echo "\nTesting POST endpoint:"
	curl -s -X POST "http://localhost:9080/base-template-shift/api/hello/count" \
		-H "Content-Type: text/plain" \
		-d "OpenTelemetry is working great" | jq . || \
	curl -s -X POST "http://localhost:9080/base-template-shift/api/hello/count" \
		-H "Content-Type: text/plain" \
		-d "OpenTelemetry is working great"

# Full development workflow
dev-setup: network-create otel-start ## Setup full development environment
	@echo "Development environment ready!"
	@echo "- OTEL/Grafana: http://localhost:3000 (admin/admin)"
	@echo "- Run 'make devc' or 'make compose-dev-up' to start the application"

dev-teardown: otel-stop compose-dev-down ## Teardown development environment
	@echo "Development environment stopped"

# Full production workflow  
prod-setup: network-create compose-up ## Setup production environment
	@echo "Production environment started!"
	@echo "- Application: http://localhost:9080/base-template-shift/api/hello"
	@echo "- Grafana: http://localhost:3000 (admin/admin)"

prod-teardown: compose-down otel-stop ## Teardown production environment
	@echo "Production environment stopped"