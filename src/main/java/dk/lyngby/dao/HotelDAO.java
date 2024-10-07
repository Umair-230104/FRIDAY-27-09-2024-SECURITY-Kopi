package dk.lyngby.dao;

import dk.lyngby.dto.HotelDTO;
import dk.lyngby.dto.RoomDTO;
import dk.lyngby.entities.Hotel;
import dk.lyngby.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class HotelDAO extends DAO
{

    private final EntityManagerFactory emf;

    public HotelDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    @Override
    public List<Hotel> getAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT h FROM Hotel h LEFT JOIN FETCH h.rooms", Hotel.class).getResultList();
        }
    }

    @Override
    public Hotel getById(long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Hotel hotel = em.createQuery("SELECT h FROM Hotel h LEFT JOIN FETCH h.rooms WHERE h.id = :id", Hotel.class)
                    .setParameter("id", id)
                    .getSingleResult();

            //Tilføjelse
            // Force initialization of the rooms collection
            hotel.getRooms().size();
            return hotel;
        }
    }

    @Override
    public void delete(long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, id);
            em.remove(hotel);
            em.getTransaction().commit();
        }
    }

    public void createHotel(Hotel hotel)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(hotel);
//            em.merge(hotel);
            em.getTransaction().commit();
        }
    }

    public void updateHotel(Hotel hotel, HotelDTO updateHotel)
    {
        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // Update hotel's basic information
            hotel.setName(updateHotel.getName());
            hotel.setAddress(updateHotel.getAddress());
            em.merge(hotel);
            em.getTransaction().commit();
        }
    }

    public List<RoomDTO> getAllRoomsByHotel(long hotelId)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.hotel.id = :hotelId", Room.class)
                    .setParameter("hotelId", hotelId)
                    .getResultList();

            //Tilføjelse
            // Convert Room entities to RoomDTOs
            return rooms.stream()
                    .map(RoomDTO::new)  // Assuming RoomDTO has a constructor that takes Room
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void create(Object o)
    {
    }

    @Override
    public void update(Object o)
    {
    }
}
