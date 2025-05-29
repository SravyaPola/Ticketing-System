<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
    <title>Ticket History</title>
</head>

<div style="font-family: 'Segoe UI', Tahoma, sans-serif; margin: 30px; color: #333;">
    <h2 style="
        font-size: 26px;
        margin-bottom: 15px;
        padding-bottom: 5px;
        border-bottom: 3px solid #4CAF50;
        letter-spacing: 0.5px;
    ">Ticket History</h2>

    <table style="
        width: 100%;
        border-collapse: collapse;
        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    ">
        <thead>
            <tr style="background-color: #4CAF50; color: white; text-transform: uppercase;">
                <th style="padding: 12px; text-align: left; font-size: 14px;">#</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Action</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Action By</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Action Date</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Comments</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entry" items="${ticketHistoryList}" varStatus="status">
                <tr
                    style="
                        background-color: ${status.index % 2 == 0 ? '#fbfbfb' : '#ffffff'};
                        transition: background-color 0.25s ease;
                    "
                    onmouseover="this.style.backgroundColor='#e8f5e9';"
                    onmouseout="this.style.backgroundColor='${status.index % 2 == 0 ? '#fbfbfb' : '#ffffff'}';"
                >
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">${status.index + 1}</td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">${entry.action}</td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">
                        ${entry.actionBy} <span style="font-style: italic; color: #666;">(${entry.role})</span>
                    </td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">
                        <fmt:formatDate value="${entry.actionDateAsDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                    </td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">${entry.comments}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
