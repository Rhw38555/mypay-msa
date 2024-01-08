package com.mypay.membership;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultToken;

@Configuration
public class VaultConfig {
    @Value("${spring.cloud.vault.token}")
    private String vaultToken;
    @Value("${spring.cloud.vault.scheme}")
    private String vaultScheme;
    @Value("${spring.cloud.vault.host}")
    private String vaultHost;
    @Value("${spring.cloud.vault.port}")
    private int vaultPort;

    @Bean
    public VaultTemplate vaultTemplate() {
        VaultEndpoint endpoint = VaultEndpoint.create(vaultHost, vaultPort);
        endpoint.setScheme(vaultScheme); // vaultScheme : http, https

        VaultTemplate vaultTemplate = new VaultTemplate(endpoint, ()-> VaultToken.of(vaultToken));
        VaultKeyValueOperations ops = vaultTemplate.opsForKeyValue(
                "kv-v1/data/encrypt",
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_2
        );

        String key = (String) ops.get("dbkey").getData().get("key");

        return new VaultTemplate(endpoint, ()-> VaultToken.of(vaultToken));
    }
}
