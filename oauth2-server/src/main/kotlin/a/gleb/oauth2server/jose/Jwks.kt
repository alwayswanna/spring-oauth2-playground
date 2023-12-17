package a.gleb.oauth2server.jose

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.RSAKey
import java.security.KeyPair
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.crypto.SecretKey


fun generateRsa(): RSAKey {
    val keyPair: KeyPair = generateRsaKey()
    val publicKey = keyPair.public as RSAPublicKey
    val privateKey = keyPair.private as RSAPrivateKey
    return RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build()
}

fun generateEc(): ECKey {
    val keyPair: KeyPair = generateEcKey()
    val publicKey = keyPair.public as ECPublicKey
    val privateKey = keyPair.private as ECPrivateKey
    val curve: Curve = Curve.forECParameterSpec(publicKey.params)

    return ECKey.Builder(curve, publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build()
}

fun generateSecret(): OctetSequenceKey {
    val secretKey: SecretKey = generateSecretKey()
    return OctetSequenceKey.Builder(secretKey)
        .keyID(UUID.randomUUID().toString())
        .build()
}