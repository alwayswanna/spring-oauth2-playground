package a.gleb.cardservice.security

import org.springframework.security.test.context.support.WithSecurityContext
import java.lang.annotation.Inherited

@Inherited
@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockSecurityContextFactory::class)
annotation class WithJwtUser(val username: String = "admin")