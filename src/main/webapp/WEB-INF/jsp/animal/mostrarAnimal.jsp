<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="animal">

            <label>Animal <c:choose><c:when test="${!animal.esArchivado}">actual</c:when><c:otherwise>archivado</c:otherwise></c:choose></label><br></br>

            Identificador:<c:out value="${animal.identificadorAnimal}"/><br></br>
            Fecha identificación:<c:out value="${animal.fechaIdentificacion}"/><br></br>
            Fecha nacimiento:<c:out value="${animal.fechaNacimiento}"/><br></br>
            Sexo:<c:out value="${animal.sexo}"/><br></br>
            Tipo ganado:<c:out value="${animal.tipoGanado.tipoGanado}"/><br></br>
            ¿El animal ha sido comprado?:
            <c:choose><c:when test="${animal.comprado == true}">
             Sí
            <div>
            <br></br>
            Datos de la compra<br></br>
            Procedencia:<c:out value="${animal.procedencia}"/><br></br>
            Fecha entrada:<c:out value="${animal.fechaEntrada}"/><br></br>
            </div>
            </c:when>
            <c:otherwise>
            No
            </c:otherwise>
            </c:choose>
            <br></br>
            
            <c:if test="${animal.esArchivado}">
	            <label>Datos animal archivado:</label><br></br>
		            <c:choose>
			            <c:when test="${animalHistorico.fechaFallecimiento != null}">
			            Fecha fallecimiento: <c:out value="${animalHistorico.fechaFallecimiento}"/>
			            </c:when>
			            <c:otherwise>
			            Fecha Venta: <c:out value="${animalHistorico.fechaVenta}"/>
			            </c:otherwise>
		            </c:choose>
		            <br></br>
	            <c:if test="${animalHistorico.masInfo != null}">Más información: <c:out value="${animalHistorico.masInfo}"/></c:if>
               <spring:url value="/explotacion-ganadera/{expGanaderaId}/ganado/{ganadoId}/animal/{animalId}/delete" var="deleteAnimalUrl">
                  <spring:param name="animalId" value="${animal.id}"/>
                  <spring:param name="ganadoId" value="${animal.tipoGanado.id}"/>
                  <spring:param name="expGanaderaId" value="${expGanaderaId}"/>
              </spring:url>	            
	            <div class="center">
	            	<a href="${fn:escapeXml(deleteAnimalUrl)}">
	            		<button type="button" id="eliminarAnimal" class="small-padding btn btn-danger">Eliminar</button>
	            	</a>
	            	<p class="small-padding">*El animal será eliminado permanentemente, <strong>no se puede deshacer.<strong></p>
	            </div>
	           
            </c:if>
            
            <c:if test="${!animal.esArchivado}">   
	            <sec:authorize access="hasAnyAuthority('veterinario')">
		            <spring:url value="animal-historico/new" var="animalHistoricoUrl"/>
	            <a href="${fn:escapeXml(animalHistoricoUrl)}"><input class="btn btn-default" type="button" value="Archivar animal"/></a>
	            </sec:authorize>
            </c:if>
            
</petclinic:layout>