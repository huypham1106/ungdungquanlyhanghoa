<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              <div class="menu_section">
                <h3>General</h3>
                <ul class="nav side-menu">
                <c:forEach items="${menuSession}" var="menu">
                  <li id="${menu.idMenu}"><a> ${menu.name} <span class="fa fa-chevron-down"></span></a> <!-- menu cha -->
                    <ul class="nav child_menu">
                    <c:forEach items="${menu.child}" var="child">
                      <li id="${child.idMenu}"><a href="<c:url value="${child.url}"/>">${child.name}</a></li> <!-- 3 menu con --> 
                     </c:forEach> 
                    </ul>
                  </li>
                  
                </c:forEach>                     
                  <li><a href="javascript:void(0)"><i class="fa fa-laptop"></i> Landing Page <span class="label label-success pull-right">Coming Soon</span></a></li>
                </ul>
              </div>

            </div>