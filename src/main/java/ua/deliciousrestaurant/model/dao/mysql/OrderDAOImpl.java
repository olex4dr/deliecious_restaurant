package ua.deliciousrestaurant.model.dao.mysql;

import ua.deliciousrestaurant.exception.DaoException;
import ua.deliciousrestaurant.model.connection.DataSource;
import ua.deliciousrestaurant.model.dao.DaoFactory;
import ua.deliciousrestaurant.model.dao.OrderDAO;
import ua.deliciousrestaurant.model.entity.Cart;
import ua.deliciousrestaurant.model.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ua.deliciousrestaurant.constant.DBConstant.*;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean insertOrder(Order order) throws DaoException {

        try (Connection con = DataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(INSERT_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, order.getClientId());
            pst.setInt(2, order.getStatusId());
            pst.setInt(3, order.getOrderTotalPrice());
            pst.setString(4, order.getAddressDelivery());
            pst.setString(5, order.getDate());
            pst.setInt(6, order.isOrderLiked() ? 1 : 0);

            if (pst.executeUpdate() > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setOrderId(rs.getInt(1));
                    }
                }

            } else {
                System.out.println("ERROR");
                return false;
            }

            if (!insertProductsIntoOrder(order)) {
                System.out.println("ERROR");
                return false;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return true;
    }

    @Override
    public List<Order> getClientOrders(int id) throws DaoException {

        List<Order> orders = new ArrayList<>();

        try (Connection con = DataSource.getConnection()) {
            PreparedStatement pst = con.prepareStatement(GET_CLIENT_ORDERS);
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(Order.builder()
                            .orderId(rs.getInt(1))
                            .clientId(rs.getInt(2))
                            .statusId(rs.getInt(3))
                            .addressDelivery(rs.getString(4))
                            .isOrderLiked(rs.getInt(5) == 1)
                            .date(rs.getString(7))
                            .build());
                }
            }

            if (!orders.isEmpty()) {
                pst = con.prepareStatement(GET_PRODUCTS_FROM_ORDER);

                for (Order order : orders) {
                    pst.setInt(1, order.getOrderId());
                    List<Cart> products = new ArrayList<>();

                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            products.add(Cart.builder()
                                    .product(DaoFactory.getInstance().getProductDAO().getProductById(rs.getInt(2)).get())
                                    .quantity(rs.getInt(3))
                                    .build());
                        }
                    }
                    order.setOrderProducts(products);
                    pst.clearParameters();
                }
            }

            pst.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }


        return orders;
    }

    @Override
    public boolean updateLikedStatus(int orderId, int isLikedStatus) throws DaoException {

        try (Connection con = DataSource.getConnection();
        PreparedStatement pst = con.prepareStatement(UPDATE_LIKED_STATUS_ORDER)){
//todo
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return true;
    }

    private boolean insertProductsIntoOrder(Order order) {

        try (Connection con = DataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(INSERT_PRODUCT_INTO_ORDER)) {

            for (Cart cart : order.getOrderProducts()) {
                pst.setInt(1, order.getOrderId());
                pst.setInt(2, cart.getProduct().getIdProduct());
                pst.setInt(3, cart.getQuantity());

                if (pst.executeUpdate() <= 0) {
                    return false;
                }

                pst.clearParameters();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}
