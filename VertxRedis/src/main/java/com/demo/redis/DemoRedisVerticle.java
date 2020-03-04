package com.demo.redis;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

public class DemoRedisVerticle extends AbstractVerticle {
    private Router router;
    private RedisClient redisClient;

    @Override
    public void start() throws Exception {

        router = Router.router(vertx);
        HttpServer httpServer = vertx.createHttpServer();

        RedisOptions redisOptions = new RedisOptions().setHost("127.0.0.1").setPort(6379);

        redisClient = RedisClient.create(vertx, redisOptions);

        router.get("/employees/:offset/:limit").handler(this::addEmployee);
        router.post("/employees/:name").handler(this::getEmployees);

        httpServer.requestHandler(router::accept).listen(9091);
    }

    private void getEmployees(RoutingContext routingContext){
        String name = routingContext.request().getParam("name");
        redisClient.lpush("employees", name, handler -> {
            if (handler.succeeded()) {
                routingContext.response().end("success");
            } else {
                routingContext.response().end("fail");
            }
        });
    }

    private void addEmployee(RoutingContext routingContext){
        long offset = Long.parseLong(routingContext.request().params().get("offset"));
        long limit = Long.parseLong(routingContext.request().params().get("limit"));
        redisClient.lrange("employees",offset, limit, jsonArrayAsyncResult -> {
            routingContext.response().end(jsonArrayAsyncResult.result().encodePrettily());
        } );
    }
}
