package ua.deliciousrestaurant.controller.action;

import lombok.Getter;
import lombok.Setter;
import ua.deliciousrestaurant.controller.action.impl.*;

import java.util.HashMap;
import java.util.Map;

import static ua.deliciousrestaurant.constant.ActionConstant.*;

public final class ActionFactory {

    private static ActionFactory actionFactory;
    private static Map<String, Action> actionMap = new HashMap<>();

    public static ActionFactory getInstance() {
        if (actionFactory == null) {
            actionFactory = new ActionFactory();

            actionMap.put(ACTION_DEFAULT, new DefaultAction());

            actionMap.put(ACTION_LOGIN, new LoginAction());
            actionMap.put(ACTION_SIGN_UP, new SignUpAction());
            actionMap.put(ACTION_LOGOUT, new LogoutAction());

            actionMap.put(ACTION_ADD_TO_CART, new AddToCartAction());
            actionMap.put(ACTION_ORDER_NOW, new OrderNowAction());
            actionMap.put(ACTION_REMOVE_FROM_CART, new RemoveFromCartAction());
            actionMap.put(ACTION_INC_DEC_QUANTITY, new QuantityIncDecAction());
            actionMap.put(ACTION_ORDER_ALL, new OrderAllAction());
            actionMap.put(ACTION_VIEW_MENU, new ViewMenuAction());
            actionMap.put(ACTION_VIEW_ORDERS_FOR_USER, new ViewOrdersForUserAction());
            actionMap.put(ACTION_SET_LIKE_FOR_ORDER, new SetLikeForOrderAction());

        }

        return actionFactory;
    }

    private ActionFactory() { }

    public Action createAction(String action) {
        return actionMap.getOrDefault(action, new DefaultAction());
    }


}