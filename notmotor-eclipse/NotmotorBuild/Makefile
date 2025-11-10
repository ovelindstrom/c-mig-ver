# Makefile for analyzing artifacts with WebSphere Application Migration Toolkit (WAMT)
# Usage: make analyze-all or make analyze-{target}

# Configuration
WAMT_JAR = ./wamt/binaryAppScanner.jar
BIN_DIR = bin
REPORTS_DIR = reports

# WAMT Analysis Parameters
WAMT_FLAGS = --all --targetAppServer=openLiberty --targetJava=java11 --targetJavaEE=ee7

# Artifact files
EAR_FILES = $(wildcard $(BIN_DIR)/*.ear)
WAR_FILES = $(wildcard $(BIN_DIR)/*.war)
ALL_FILES = $(EAR_FILES) $(WAR_FILES)

# Generate report file names
EAR_REPORTS = $(patsubst $(BIN_DIR)/%.ear,$(REPORTS_DIR)/%.html,$(EAR_FILES))
WAR_REPORTS = $(patsubst $(BIN_DIR)/%.war,$(REPORTS_DIR)/%.html,$(WAR_FILES))
ALL_REPORTS = $(EAR_REPORTS) $(WAR_REPORTS)

# Default target
.PHONY: all
all: analyze-all

# Analyze all artifacts
.PHONY: analyze-all
analyze-all: $(ALL_REPORTS)
	@echo "=== WAMT Analysis Complete ==="
	@echo "Reports generated in $(REPORTS_DIR)/"
	@ls -la $(REPORTS_DIR)/

# Create reports directory
$(REPORTS_DIR):
	mkdir -p $(REPORTS_DIR)

# Analyze EAR files
$(REPORTS_DIR)/%.html: $(BIN_DIR)/%.ear | $(REPORTS_DIR)
	@echo "Analyzing EAR: $<"
	java -jar $(WAMT_JAR) $< $(WAMT_FLAGS) --output=$@
	@echo "Report generated: $@"

# Analyze WAR files  
$(REPORTS_DIR)/%.html: $(BIN_DIR)/%.war | $(REPORTS_DIR)
	@echo "Analyzing WAR: $<"
	java -jar $(WAMT_JAR) $< $(WAMT_FLAGS) --output=$@
	@echo "Report generated: $@"

# Individual artifact targets
.PHONY: analyze-ipl-ear
analyze-ipl-ear: $(REPORTS_DIR)/notmotor-ipl-ear-1.0-SNAPSHOT.html

.PHONY: analyze-admin-ear
analyze-admin-ear: $(REPORTS_DIR)/notmotor-admin-ear-1.0-SNAPSHOT.html

.PHONY: analyze-ipl-webb
analyze-ipl-webb: $(REPORTS_DIR)/notmotor-ipl-webb-1.0-SNAPSHOT.html

.PHONY: analyze-admin-webb
analyze-admin-webb: $(REPORTS_DIR)/notmotor-admin-webb-1.0-SNAPSHOT.html

# Clean reports
.PHONY: clean
clean:
	rm -rf $(REPORTS_DIR)
	@echo "Reports directory cleaned"

# Rebuild artifacts and analyze
.PHONY: rebuild-analyze
rebuild-analyze: clean-build build analyze-all

.PHONY: clean-build
clean-build:
	cd .. && mvn clean

.PHONY: build
build:
	cd .. && mvn package -DskipTests

# Show help
.PHONY: help
help:
	@echo "Notmotor WAMT Analysis Makefile"
	@echo "================================"
	@echo ""
	@echo "Targets:"
	@echo "  analyze-all       - Analyze all EAR and WAR files"
	@echo "  analyze-ipl-ear   - Analyze IPL EAR file only"
	@echo "  analyze-admin-ear - Analyze Admin EAR file only"
	@echo "  analyze-ipl-webb  - Analyze IPL WAR file only"
	@echo "  analyze-admin-webb- Analyze Admin WAR file only"
	@echo "  rebuild-analyze   - Clean build and analyze all"
	@echo "  clean            - Remove all report files"
	@echo "  help             - Show this help message"
	@echo ""
	@echo "Configuration:"
	@echo "  WAMT_JAR: $(WAMT_JAR)"
	@echo "  Target: OpenLiberty + Java 11 + JavaEE 7"
	@echo ""
	@echo "Reports will be generated in: $(REPORTS_DIR)/"

# Check if WAMT jar exists
.PHONY: check-wamt
check-wamt:
	@if [ ! -f "$(WAMT_JAR)" ]; then \
		echo "ERROR: WAMT jar not found at $(WAMT_JAR)"; \
		echo "Please ensure the WebSphere Application Migration Toolkit is installed"; \
		exit 1; \
	else \
		echo "WAMT jar found: $(WAMT_JAR)"; \
	fi

# Show current artifacts
.PHONY: show-artifacts
show-artifacts:
	@echo "Available artifacts in $(BIN_DIR):"
	@ls -la $(BIN_DIR)/ 2>/dev/null || echo "No artifacts found. Run 'make build' first."

# Validate setup before analysis
analyze-all: check-wamt show-artifacts