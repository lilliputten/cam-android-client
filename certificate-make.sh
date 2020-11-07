#!/bin/sh
# @descr Create self-signed SSL certificate for https server
# @see [Generating a self-signed certificate using OpenSSL](https://www.ibm.com/support/knowledgecenter/SSMNED_5.0.0/com.ibm.apic.cmc.doc/task_apionprem_gernerate_self_signed_openSSL.html)
# @since 2020.08.07, 10:18
# @changed 2020.11.08, 01:17

# Certificate password:
CERT_PWD="123"

# Certificate params (Don't forget to change target machine address (on line 6):
CERT_PARAMS_FILE="certificate-params.txt"

# Files:
CERT_FILE_PEM="certificate.pem"
CERT_FILE_KEY="certificate.key"
CERT_FILE_P12="certificate.p12"

DELIM="--------------------------------------------------------------------------------"

rm -f "$CERT_FILE_PEM"
rm -f "$CERT_FILE_KEY"
rm -f "$CERT_FILE_P12"

echo "Creating certificate files with params (Country, State, Location, Org, Unit, CN, Email):" \
&& echo "$DELIM" \
&& cat "$CERT_PARAMS_FILE" \
&& echo "$DELIM" \
&& echo "Generate your private key and public certificate:" \
&& openssl req -newkey rsa:2048 -nodes -keyout "$CERT_FILE_KEY" -x509 -days 365 -out "$CERT_FILE_PEM" < "$CERT_PARAMS_FILE" \
&& echo "Review the created certificate:" \
&& openssl x509 -text -noout -in "$CERT_FILE_PEM" \
&& echo "Combine key and certificate in a PKCS#12 (P12) bundle:" \
&& openssl pkcs12 -inkey "$CERT_FILE_KEY" -in "$CERT_FILE_PEM" -export -out "$CERT_FILE_P12" -passout pass:$CERT_PWD \
&& echo "Validate P2 file:" \
&& openssl pkcs12 -in "$CERT_FILE_P12" -noout -info -passin pass:$CERT_PWD \
&& echo "$DELIM" \
&& echo "pem file: $CERT_FILE_PEM" \
&& echo "key file: $CERT_FILE_KEY" \
&& echo "p12 file: $CERT_FILE_P12" \
&& echo "$DELIM" \
&& echo "OK"
