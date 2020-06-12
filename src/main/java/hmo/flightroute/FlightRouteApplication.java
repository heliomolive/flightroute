package hmo.flightroute;

import hmo.flightroute.domain.Route;
import hmo.flightroute.service.RouteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.util.Objects.nonNull;

@SpringBootApplication
public class FlightRouteApplication {

	public static void main(String[] args) {
		try {
			ApplicationContext applicationContext = SpringApplication.run(FlightRouteApplication.class, args);

			if (args.length>0) {
				processConsole(applicationContext.getBean(RouteService.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void processConsole(RouteService routeService) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println();
			System.out.println("#=================================================#");
			System.out.print("Please enter the route (SRC-DEST): ");

			String[] location = parseStdinLine(reader.readLine());
			if (nonNull(location)) {
				Route route = routeService.findRoute(location[0], location[1]);
				System.out.println("Cheapest route: " + route.toString());
			}
			System.out.println("#=================================================#");
		}
	}

	private static String[] parseStdinLine(String line) {
		String[] route = line.split("-");
		if (route.length!=2) {
			System.out.println("Invalid input route format! Usage: SRC-DEST");
			return null;
		}
		return route;
	}

}
