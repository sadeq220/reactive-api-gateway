package sadad.fusion.gateway.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;

public class UserDetailsPrincipal extends DefaultOidcUser {

    public UserDetailsPrincipal(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken, OidcUserInfo userInfo, String nameAttributeKey) {
        super(authorities, idToken, userInfo, nameAttributeKey);
    }
}
