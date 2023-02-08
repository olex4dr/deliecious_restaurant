<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" scope="session"/>
<fmt:setBundle basename="resources"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">
<head>
    <%@include file="includes/head.jsp" %>
    <title><fmt:message key="cart"/></title>
</head>

<body>

<jsp:include page="includes/header.jsp"/>

<c:if test="${ sessionScope.cart_list.size() <= 0 || sessionScope.cart_list == null }">
    <section class="vh-200" style="background-color: #eee;">
        <div class="container h-100 justify-content-center container-style">
            <p class="col-12 text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4" style="padding: 50px 0;">
                <fmt:message key="cart.not.selected"/>
            </p>
            <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4" style="padding: 50px 0;">
                <a type="button" class="btn btn-danger btn-lg"
                   href="controller?action=view-menu&sort_field=p.category_id&sort_order=asc&category_filter_id=0&offset=0&records=8&cur_page=1">
                    <fmt:message key="menu"/>
                </a>
            </div>
        </div>
    </section>
</c:if>

<c:if test="${ requestScope.cart_items.size() > 0}">

    <section class="vh-200 section-style" style="padding: 10px 0;">

        <div class="container h-100 container-style my-3">

            <div class="row d-flex justify-content-end my-3 px-2">

                <jsp:include page="fragments/buy_all.jsp"/>

            </div>

        </div>
    </section>

    <section class="vh-200 section-style p-1">
        <div class="container h-100 container-style" style="min-height: 400px;">
            <div class="row my-2">

                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col"><fmt:message key="cart.name"/></th>
                        <th scope="col"><fmt:message key="cart.category"/></th>
                        <th scope="col"><fmt:message key="cart.price"/></th>
                        <th scope="col" class="text-center"><fmt:message key="cart.quantity"/></th>
                        <th scope="col" class="text-center"><fmt:message key="cart.total.price"/></th>
                        <th scope="col" class="text-center"><fmt:message key="cart.buy.now"/></th>
                        <th scope="col" class="text-center"><fmt:message key="cart.cancel"/></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="cart" items="${ requestScope.cart_items }">
                        <tr>
                            <td><img class="card-img-to rounded-circle" src="${ cart.getProduct().getImgProduct() }"
                                     alt="Card image cap" style="height:50px;width: 50px"></td>
                            <td><c:out value="${ cart.getProduct().getNameProduct() }"/></td>
                            <td><c:out value="${ cart.getProduct().getCategoryName() }"/></td>
                            <td><c:out value="${ cart.getProduct().getCostProduct() }"/> <fmt:message
                                    key="currency"/></td>
                            <td>
                                <form action="order-all" method="post" class="form-inline">
                                    <input type="hidden" name="id" value="${ cart.getQuantity() }" class="form-input">
                                    <div class="form-group d-flex justify-content-center">
                                        <a class="btn btn-decre"
                                           href="controller?action=inc-dec-quantity&operation=dec&prod_id=${ cart.getProduct().getIdProduct() }"><i
                                                class="fas fa-minus-square"></i></a>
                                        <input type="text" name="quantity" class="form-control w-75 text-center"
                                               value="${ cart.getQuantity() }" readonly>
                                        <a class="btn btn-incre"
                                           href="controller?action=inc-dec-quantity&operation=inc&prod_id=${ cart.getProduct().getIdProduct() }"><i
                                                class="fas fa-plus-square"></i></a>
                                    </div>
                                </form>
                            </td>
                            <td class="text-center"><c:out
                                    value="${ cart.getProduct().getCostProduct() * cart.getQuantity() }"/> <fmt:message
                                    key="currency"/></td>
                            <td class="text-center">
                                <a class="btn btn-warning"
                                   href="controller?action=order-now&prod_id=${ cart.getProduct().getIdProduct() }&quantity=${ cart.getQuantity() }">
                                    <fmt:message key="cart.buy"/>
                                </a>
                            </td>
                            <td class="text-center">
                                <a class="btn btn-danger"
                                   href="controller?action=remove-from-cart&prod_id=${ cart.getProduct().getIdProduct() }">
                                    <fmt:message key="cart.remove"/>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>

            </div>
        </div>
    </section>

</c:if>

<jsp:include page="includes/footer.jsp"/>

<%--todo--%>
<input type="hidden" id="status" value="${ requestScope.status }">
<input type="hidden" id="locale" value="${ sessionScope.locale }">
<c:set var="status" value="none" scope="request"/>
<script type="text/javascript">
    var status = document.getElementById("status").value;
    var locale = document.getElementById("locale").value;

    if (status === "order_failed") {
        if (locale == null || locale === "en") {
            swal("Error", "Order not created!", "error");
        } else {
            swal("Помилка", "Замовлення не створено!", "error");
        }
    }

</script>
<%--todo--%>

</body>
</html>
