package ru.steamtanks;

import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.steamtanks.websockets.GameSocketHandler;


import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class Main {
    public static final long IDLE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(1);
    public static final int BUFFER_SIZE_BYTES = 8192;

    public static void main(String[] args) {
        SpringApplication.run(
                new Object[]{
                        WebSocketConfig.class,
                        Main.class
                }, args);
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        final WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        policy.setInputBufferSize(BUFFER_SIZE_BYTES);
        policy.setIdleTimeout(IDLE_TIMEOUT_MS);

        return new DefaultHandshakeHandler(
                new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
    }

    @Bean
    public WebSocketHandler gameWebSocketHandler() {
        return new PerConnectionWebSocketHandler(GameSocketHandler.class);
    }
}
