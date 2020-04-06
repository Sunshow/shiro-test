package net.sunshow.test.shiro.config;

import net.sunshow.test.shiro.framework.TestRealm;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    public static void main(String[] args) throws Exception {
        AesCipherService cipherService = new AesCipherService();
        byte[] key = cipherService.generateNewKey().getEncoded();
        System.out.println(Base64.encodeToString(key));
    }

    @Bean
    public RememberMeManager rememberMeManager() {
        Cookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600 * 24 * 7); // 7 days

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        // should specify the cipher key, otherwise will generate new key after restart
        cookieRememberMeManager.setCipherKey(Base64.decode("i45FVt72K2kLgvFrJtoZRw==")); // 128 bit AES key
        return cookieRememberMeManager;
    }

    @Bean
    public Realm realm() {
        return new TestRealm();
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // logged in users with the 'admin' role
        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");

        // logged in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");

        //chainDefinition.addPathDefinition("/", "anon");
        chainDefinition.addPathDefinition("/assets/**", "anon");

        chainDefinition.addPathDefinition("/login", "anon");

        chainDefinition.addPathDefinition("/", "user");

        // all other paths require a logged in user
        chainDefinition.addPathDefinition("/**", "authc");

        return chainDefinition;
    }
}
