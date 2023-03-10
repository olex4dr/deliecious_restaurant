package ua.deliciousrestaurant.controller.action.impl.user;

import ua.deliciousrestaurant.controller.action.Action;
import ua.deliciousrestaurant.exception.DaoException;
import ua.deliciousrestaurant.exception.ServiceException;
import ua.deliciousrestaurant.model.dao.DaoFactory;
import ua.deliciousrestaurant.model.dto.ClientDTO;
import ua.deliciousrestaurant.model.entity.Client;
import ua.deliciousrestaurant.model.entity.Role;
import ua.deliciousrestaurant.service.ServiceFactory;
import ua.deliciousrestaurant.utils.ConvertorUtil;
import ua.deliciousrestaurant.utils.PasswordCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.deliciousrestaurant.constant.ActionConstant.*;
import static ua.deliciousrestaurant.controller.action.ActionUtil.isGetMethod;
import static ua.deliciousrestaurant.controller.action.ActionUtil.setValueToRequestFromSession;

public class SignUpAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        return isGetMethod(request) ? exeGet(request) : exePost(request);
    }

    private String exeGet(HttpServletRequest request) {
        setValueToRequestFromSession(request, EMAIL);
        return PAGE_SIGN_UP;
    }

    private String exePost(HttpServletRequest request) throws ServiceException {
        String email = request.getParameter(REG_EMAIL);
        String password = request.getParameter(REG_PASSWORD);
        String firstName = request.getParameter(REG_FIRST_NAME);
        String lastName = request.getParameter(REG_LAST_NAME);
        String address = request.getParameter(REG_ADDRESS);

        if (!ServiceFactory.getInstance().getClientService().checkEmailUniq(email)) {
            request.getSession().setAttribute(VALID_STATUS, "user.exist");
            return PAGE_SIGN_UP;
        }

        Client client = Client.builder()
                .email(email)
                .password(PasswordCode.encodePassword(password).get())
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .role(Role.CLIENT)
                .build();

        try {

            client.setClientId(DaoFactory.getInstance().getClientDAO().addClient(client));

            ClientDTO clientDTO = ConvertorUtil.convertUserToDTO(client);

            System.out.println(clientDTO);

            setLoggedUser(request, clientDTO);

        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        return INDEX_PAGE;
    }

    private static void setLoggedUser(HttpServletRequest request, ClientDTO client) {
        request.getSession().setAttribute(AUTH, client);
        request.getSession().setAttribute(ROLE, client.getRole());
    }
}
