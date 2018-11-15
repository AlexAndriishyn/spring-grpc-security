prerequisites
openssl

keys recommendations
at least 2048 bit keys with RS256 and PS256
at least 3072 bit keys with RS384 and PS384
at least 4096 bit keys with RS512 and PS512

generate an RSA keypair
openssl genpkey -out private_key.pkcs8.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048

extract the public key from RSA keypair
openssl rsa -pubout -in private_key.pkcs8.pem -out public_key.pkcs8.pem

view private key structure
openssl rsa -text -in private_key.pkcs8.pem

Always verify the signature before you trust any information in the JWT