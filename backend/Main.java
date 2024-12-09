import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static List<Task> taskList = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        // Create HTTP server
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler());
        server.setExecutor(null); 
        server.start();
        System.out.println("Server running on port: 8080...");
    }

    // Handler HTTP
    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            //Indentify Method (GET or POST)
            String method = exchange.getRequestMethod();
            if ("GET".equalsIgnoreCase(method)){
                handleGet(exchange);
            } else if ("POST".equalsIgnoreCase(method)){
                handlePost(exchange);
            }
        }
        
        // GET METHOD, return all tasks
        private void handleGet(HttpExchange exchange) throws IOException{
            String response = taskListToJson();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        // POST METHOD, add new tasks
        private void handlePost(HttpExchange exchange) throws IOException{
            // Read body's requirement
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer requestBody = new StringBuffer();

            String line;

            while ((line = reader.readLine()) != null){
                requestBody.append(line);
            }

            // Create a new task
            Task newTask = new Task(String.valueOf(taskList.size() + 1), requestBody.toString(), false);
            taskList.add(newTask);

            String response = "Task Added!";
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String taskListToJson(){
            StringBuilder json = new StringBuilder("[");
            for(int i = 0; i < taskList.size(); i++){
                Task task = taskList.get(i);
                json.append("{\"id\":\"").append(task.getId())
                    .append("\",\"description\":\"").append(task.getDescription())
                    .append("\",\"completed\":").append(task.isCompleted())
                    .append("}");
                if (i < taskList.size() - 1) {
                    json.append(",");
                }
            } 
            json.append("]");
            return json.toString();
        }
    }
}
