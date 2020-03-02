package com.vertx.demo.rest;

import com.vertx.demo.entity.Category;
import com.vertx.demo.repository.CategoryRepository;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class App {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);

        CategoryRepository categoryRepository = new CategoryRepository();

        /**
         * GET categories
         * */
        router.get("/category")
                .produces("*/json")
                .handler(routingContext -> {
                    routingContext.response().setChunked(true).end(Json.encodePrettily(categoryRepository.findAll()));
                });


        /**
         * GET category by id
         * params: id - long
         * */
        router.get("/category/:id")
                .produces("*/json")
                .handler(routingContext -> {
                    Long id = Long.valueOf(routingContext.request().getParam("id"));
                    routingContext.response().setChunked(true).end(Json.encodePrettily(categoryRepository.findById(id)));
                });

        /**
         * POST category
         * */
        router.post("/category")
                .consumes("*/json")
                .handler(routingContext -> {
                    routingContext.request().bodyHandler(bodyHandler -> {
                        final JsonObject body = bodyHandler.toJsonObject();
                        System.out.println("post: " + body.toString());
                        Category category = new Category(body.getLong("id"), body.getString("name"));
                        category = categoryRepository.add(category);
                        if (category != null) {
                            routingContext.response().end(Json.encodePrettily(category));
                        }else {
                            routingContext.response().end(Json.encodePrettily(null));
                        }
                    });
                });

        httpServer.requestHandler(router::accept).listen(9090);


//        WebClient webClient = WebClient.create(vertx);
//
//        webClient.get(9090, "localhost", "category").send(res -> {
//            if (res.succeeded()){
//                HttpResponse<Buffer> httpResponse = res.result();
//                System.out.println("Response Api: ");
//                System.out.println(httpResponse.bodyAsString());
//            }else {
//                System.err.println("Error: "+ res.cause().getMessage());
//            }
//        });

    }


}
