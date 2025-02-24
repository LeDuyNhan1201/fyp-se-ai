# CA
````shell
openssl genrsa -out certs/CA/ca.key 2048
openssl req -new -x509 -key certs/CA/ca.key -out certs/CA/ca.crt -days 3650 -subj "/CN=novax.com/OU=IT/O=SGU/L=HCM/ST=BinhTan/C=VN" -passin pass:Novax@123 -passout pass:Novax@123
````
# Kafka brokers
**1. Create a server certificate signed by the CA**
````shell
openssl genrsa -out certs/kafka/kafka1.key 2048
openssl req -new -key certs/kafka/kafka1.key -out certs/kafka/kafka1.csr -config certs/kafka/kafka1.cnf
openssl x509 -req -in certs/kafka/kafka1.csr -CA certs/CA/ca.crt -CAkey certs/CA/ca.key -CAcreateserial -out certs/kafka/kafka1.crt -days 365 -extfile certs/kafka/kafka1.cnf -extensions req_ext

openssl genrsa -out certs/kafka/kafka2.key 2048
openssl req -new -key certs/kafka/kafka2.key -out certs/kafka/kafka2.csr -config certs/kafka/kafka2.cnf
openssl x509 -req -in certs/kafka/kafka2.csr -CA certs/CA/ca.crt -CAkey certs/CA/ca.key -CAcreateserial -out certs/kafka/kafka2.crt -days 365 -extfile certs/kafka/kafka2.cnf -extensions req_ext

openssl genrsa -out certs/kafka/kafka3.key 2048
openssl req -new -key certs/kafka/kafka3.key -out certs/kafka/kafka3.csr -config certs/kafka/kafka3.cnf
openssl x509 -req -in certs/kafka/kafka3.csr -CA certs/CA/ca.crt -CAkey certs/CA/ca.key -CAcreateserial -out certs/kafka/kafka3.crt -days 365 -extfile certs/kafka/kafka3.cnf -extensions req_ext
````
**2. Create the keystore**
````shell
openssl pkcs12 -export -in certs/kafka/kafka1.crt -inkey certs/kafka/kafka1.key -out certs/kafka/kafka1.p12 -name kafka1-cert
keytool -importkeystore -destkeystore certs/kafka/kafka1.keystore.jks -srckeystore certs/kafka/kafka1.p12 -srcstoretype PKCS12 -alias kafka1-cert -deststorepass Novax@123

openssl pkcs12 -export -in certs/kafka/kafka2.crt -inkey certs/kafka/kafka2.key -out certs/kafka/kafka2.p12 -name kafka2-cert
keytool -importkeystore -destkeystore certs/kafka/kafka2.keystore.jks -srckeystore certs/kafka/kafka2.p12 -srcstoretype PKCS12 -alias kafka2-cert -deststorepass Novax@123

openssl pkcs12 -export -in certs/kafka/kafka3.crt -inkey certs/kafka/kafka3.key -out certs/kafka/kafka3.p12 -name kafka3-cert
keytool -importkeystore -destkeystore certs/kafka/kafka3.keystore.jks -srckeystore certs/kafka/kafka3.p12 -srcstoretype PKCS12 -alias kafka3-cert -deststorepass Novax@123
````
**3. Create the truststore and import the CA certificate**
````shell
keytool -import -file certs/CA/ca.crt -alias ca-cert -keystore certs/kafka/kafka1.truststore.jks -storepass Novax@123

keytool -import -file certs/CA/ca.crt -alias ca-cert -keystore certs/kafka/kafka2.truststore.jks -storepass Novax@123

keytool -import -file certs/CA/ca.crt -alias ca-cert -keystore certs/kafka/kafka3.truststore.jks -storepass Novax@123
````
# Schema registry
**1. Create a server certificate signed by the CA**
````shell
openssl genrsa -out certs/schema-registry/schemaregistry.key 2048
openssl req -new -key certs/schema-registry/schemaregistry.key -out certs/schema-registry/schemaregistry.csr -config certs/schema-registry/schema-registry.cnf
openssl x509 -req -in certs/schema-registry/schemaregistry.csr -CA certs/CA/ca.crt -CAkey certs/CA/ca.key -CAcreateserial -out certs/schema-registry/schemaregistry.crt -days 365 -extfile certs/schema-registry/schema-registry.cnf -extensions req_ext
````
**2. Create the keystore**
````shell
openssl pkcs12 -export -in certs/schema-registry/schemaregistry.crt -inkey certs/schema-registry/schemaregistry.key -out certs/schema-registry/schemaregistry.p12 -name schema-registry-cert
keytool -importkeystore -destkeystore certs/schema-registry/kafka.schemaregistry.keystore.jks -srckeystore certs/schema-registry/schemaregistry.p12 -srcstoretype PKCS12 -alias schema-registry-cert -deststorepass Novax@123
````
**3. Create the truststore and import the CA certificate**
````shell
keytool -import -file certs/CA/ca.crt -alias ca-cert -keystore certs/schema-registry/kafka.schemaregistry.truststore.jks -storepass Novax@123
````
# Auth Service
**1. Create a server certificate signed by the CA**
````shell
openssl genrsa -out certs/auth/auth.key 2048
openssl req -new -key certs/auth/auth.key -out certs/auth/auth.csr -passin pass:Novax@123 -subj "/CN=localhost/OU=IT/O=SGU/L=HCM/ST=BinhTan/C=VN"
openssl x509 -req -in certs/auth/auth.csr -CA certs/CA/ca.crt -CAkey certs/CA/ca.key -CAcreateserial -out certs/auth/auth.crt -days 365
````
**2. Create the keystore**
````shell
openssl pkcs12 -export -in certs/auth/auth.crt -inkey certs/auth/auth.key -out certs/auth/keystore.p12 -name auth-cert
````
**3. Create the truststore and import the CA certificate**
````shell
keytool -import -file certs/CA/ca.crt -alias ca-cert -keystore certs/auth/truststore.p12 -storetype PKCS12 -storepass Novax@123
````
