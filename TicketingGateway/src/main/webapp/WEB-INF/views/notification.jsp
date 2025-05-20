<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<style>
  /* Simple positioning and style */
  #notif-menu { position: relative; display: inline-block; }
  #notif-icon { color: #333; text-decoration: none; position: relative; }
  #notif-count {
    position: absolute;
    top: -5px; right: -10px;
    background: red;
    color: white;
    border-radius: 50%;
    padding: 2px 6px;
    font-size: 0.75em;
  }
  #notif-list {
    display: none;
    position: absolute;
    right: 0;
    background: white;
    border: 1px solid #ccc;
    width: 300px;
    max-height: 400px;
    overflow-y: auto;
    z-index: 1000;
  }
  #notif-list a { display: block; padding: 8px; text-decoration: none; color: #333; }
  #notif-list a:hover { background: #f5f5f5; }
</style>

<div id="notif-menu">
  <a href="#" id="notif-icon">
    <i class="fa fa-bell fa-lg"></i>
    <span id="notif-count">0</span>
  </a>
  <div id="notif-list"></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
  (function(){
    // Replace with your gateway or service host/port
    const API_BASE = '${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}';
    const USER_ID = ${currentUser.id};

    // Fetch and render unread count
    function refreshCount() {
      $.get(`${API_BASE}/api/notifications/count`, { userId: USER_ID })
       .done(count => $('#notif-count').text(count))
       .fail(() => console.error('Failed to fetch notif count'));
    }

    // Fetch and render unread list
    function loadNotifications() {
      $.get(`${API_BASE}/api/notifications`, { userId: USER_ID })
       .done(list => {
         const $menu = $('#notif-list').empty();
         if (!list.length) {
           $menu.append('<a href="#">No new notifications</a>');
         } else {
           list.forEach(n => {
             $menu.append(`
               <a href="${n.url}" data-id="${n.id}">
                 ${n.message}
                 <br><small style="color:#666;">${new Date(n.createdAt).toLocaleString()}</small>
               </a>`);
           });
         }
       })
       .fail(() => console.error('Failed to load notifications'));
    }

    // Toggle dropdown on icon click
    $('#notif-icon').on('click', function(e){
      e.preventDefault();
      const $list = $('#notif-list').toggle();
      if ($list.is(':visible')) {
        loadNotifications();
      }
    });

    // Mark as read when clicking an item
    $(document).on('click', '#notif-list a[data-id]', function(){
      const id = $(this).data('id');
      $.post(`${API_BASE}/api/notifications/${id}/read`, { userId: USER_ID })
       .done(() => refreshCount())
       .fail(() => console.error('Failed to mark read'));
    });

    // Poll as a fallback every minute
    refreshCount();
    setInterval(refreshCount, 60000);

    // WebSocket push
    const socket  = new SockJS(`${API_BASE}/ws`);
    const stomp   = Stomp.over(socket);
    stomp.connect({}, function() {
      stomp.subscribe(`/topic/notify/${USER_ID}`, function(msg) {
        const n = JSON.parse(msg.body);
        // increment badge
        let c = +$('#notif-count').text() || 0;
        $('#notif-count').text(c + 1);
        // prepend to list if open
        if ($('#notif-list').is(':visible')) {
          $('#notif-list').prepend(`
            <a href="${n.url}" data-id="${n.id}">
              ${n.message}
              <br><small style="color:#666;">${new Date(n.createdAt).toLocaleString()}</small>
            </a>`);
        }
      });
    }, function(err){
      console.error('STOMP error', err);
    });
  })();
</script>
