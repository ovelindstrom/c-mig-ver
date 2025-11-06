
FROM icr.io/appcafe/open-liberty:kernel-slim-java25-openj9-ubi-minimal
ARG VERSION=1.0
ARG REVISION=SNAPSHOT

LABEL \
  org.opencontainers.image.authors="CSN Developers" \
  org.opencontainers.image.vendor="CSN" \
  org.opencontainers.image.url="local" \
  org.opencontainers.image.version="$VERSION" \
  org.opencontainers.image.revision="$REVISION" \
  vendor="CSN" \
  name="notmotorn" \
  version="$VERSION-$REVISION" \
  summary="Notmotorn som Open Liberty" \
  description="Den här container kör Nomotorns IPL och Webklient."

COPY --chown=1001:0 src/main/liberty/config /config

RUN features.sh

COPY --chown=1001:0 target/liberty/wlp/usr/servers/notmotorn/apps/*.ear /config/apps/

COPY --chown=1001:0 target/liberty/wlp/usr/servers/notmotorn/lib/ /config/lib/

RUN configure.sh
USER 1001

