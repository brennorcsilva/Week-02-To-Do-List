import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Main {
    public static void main(String[] args) throws Exception {
        // Criar servidor HTTP na porta 8080
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler());
        server.setExecutor(null); // cria um executor padrão
        server.start();
        System.out.println("Servidor rodando na porta 8080...");
    }

    // Handler que vai lidar com as requisições HTTP
    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Tarefa recebida!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
