package com.example.library.client.web;

import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class BooksController {

  private final WebClient webClient;

  @Value("${library.server}")
  private String libraryServer;

  public BooksController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/")
  Mono<String> index(@AuthenticationPrincipal OidcUser oidcUser, Model model) {

    model.addAttribute("fullname", oidcUser.getName());
    model.addAttribute(
        "isCurator",
        ((ArrayList) oidcUser.getClaim("groups")).get(0).equals("library_curator"));
    return webClient
        .get()
        .uri(libraryServer + "/books")
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatusCode::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            cr -> Mono.just(new Exception(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .bodyToMono(BookListResource.class)
        .log()
        .map(BookListResource::get_embedded)
        .map(EmbeddedBookListResource::getBookResourceList)
        .map(
            c -> {
              model.addAttribute("books", c);
              return "index";
            });
  }

  @GetMapping("/createbook")
  String createForm(Model model) {

    model.addAttribute("book", new CreateBookResource());

    return "createbookform";
  }

  @PostMapping("/create")
  String create(
      CreateBookResource createBookResource,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model)
      throws IOException {

    webClient
        .post()
        .uri(libraryServer + "/books")
        .body(Mono.just(createBookResource), CreateBookResource.class)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatusCode::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            cr -> Mono.just(new Exception(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .block();

    response.sendRedirect(request.getContextPath());
    return null;
  }

  @GetMapping("/borrow")
  String borrowBook(
      @RequestParam("identifier") String identifier,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    webClient
        .post()
        .uri(libraryServer + "/books/{bookId}/borrow", identifier)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatusCode::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .onStatus(
                HttpStatusCode::is5xxServerError,
            cr -> Mono.just(new Exception(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .block();

    response.sendRedirect(request.getContextPath());
    return null;
  }

  @GetMapping("/return")
  String returnBook(
      @RequestParam("identifier") String identifier,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    webClient
        .post()
        .uri(libraryServer + "/books/{bookId}/return", identifier)
        .retrieve()
        .onStatus(
            s -> s.equals(HttpStatus.UNAUTHORIZED),
            cr -> Mono.just(new BadCredentialsException("Not authenticated")))
        .onStatus(
            HttpStatusCode::is4xxClientError,
            cr -> Mono.just(new IllegalArgumentException(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            cr -> Mono.just(new Exception(HttpStatus.valueOf(cr.statusCode().value()).getReasonPhrase())))
        .bodyToMono(BookResource.class)
        .log()
        .block();

    response.sendRedirect(request.getContextPath());
    return null;
  }
}
