FROM debezium/postgres:17-alpine

ENV  WAL2JSON_TAG=wal2json_2_6

RUN apk add --no-cache --virtual .debezium-build-deps gcc clang15 llvm15 git make musl-dev pkgconf \
    && git clone https://github.com/eulerto/wal2json -b master --single-branch \
    && (cd /wal2json && git checkout tags/$WAL2JSON_TAG -b $WAL2JSON_TAG && make && make install) \
    && (rm -rf wal2json && [ ! -d "wal2json" ] && echo "wal2json removed successfully") \
    && apk del .debezium-build-deps