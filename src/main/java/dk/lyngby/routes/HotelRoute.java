package dk.lyngby.routes;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.controller.HotelController;
import dk.lyngby.dao.HotelDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoute
{
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
    private final HotelDAO hotelDAO = new HotelDAO(emf);
    private final HotelController hotelController = new HotelController(hotelDAO);

    public EndpointGroup getHotelRoutes()
    {
        return () ->
        {
            get("/", hotelController::getAllHotels);
            get("/{id}", hotelController::getHotelById);
            post("/", hotelController::createHotel);
            delete("/{id}", hotelController::deleteHotel);
            put("/{id}", hotelController::updateHotel);
            get("/rooms/{id}", hotelController::roomForSpecificHotel);
        };
    }

}
