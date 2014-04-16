<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
           prefix="c" %>
<ul>
<c:forEach var="city" items="${cities}">
  <li>${city}</li>
</c:forEach>
</ul>