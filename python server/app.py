from flask import Flask, render_template, request
from flask_socketio import SocketIO, send, emit, join_room, leave_room
from collections import defaultdict
from random import choice

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secretkey'
app.config['DEBUG'] = True
socketio = SocketIO(app, cors_allowed_origins='*', logger=True, engineio_logger=True)


class client(object):
    def __init__(self, id):
        self.id = id
        self.room = None #not assigned to room




users = {}


max_user_per_room = 2
saved_board = defaultdict(dict)
ready_rooms = defaultdict(list)
rooms = defaultdict(list)


@socketio.on('connect')
def connect():
    print('connect ', request.sid)


@socketio.on('disconnect')
def disconnect():
    print('disconnected ', request.sid)

    emit('roommatesLeft', request.sid, broadcast=False, include_self=False, room=users[str(request.sid)])

    if str(request.sid) in users:

        if str(request.sid) in ready_rooms[users[str(request.sid)]]:
            ready_rooms[users[str(request.sid)]].remove(str(request.sid))
            if not ready_rooms[users[str(request.sid)]]:
                del ready_rooms[users[str(request.sid)]]

        if str(request.sid) in saved_board[users[str(request.sid)]]:
            del saved_board[users[str(request.sid)]][str(request.sid)]
            if not saved_board[users[str(request.sid)]]:
                del saved_board[users[str(request.sid)]]


        rooms[users[str(request.sid)]].remove(request.sid)
        if not rooms[users[str(request.sid)]]:
            del rooms[users[str(request.sid)]]
        del users[str(request.sid)]
        print(rooms)
        print(users)
        print(saved_board)
        print(ready_rooms)




@socketio.on('message from user')
def receive_message_from_user(message):
    print('USER MESSAGE: {}'.format(message))
    emit('from flask', message.upper(), broadcast=True)

@socketio.on('username')
def receive_username(username):
    username = str(username)
    print(f'Username added! Welcome {username} your sid is {request.sid} saved_board:{saved_board}')
    unique_id = str(request.sid)
    id_room = str(len(rooms))
    for room_id, user_list in rooms.items():
        if len(user_list) < max_user_per_room:
            #room not filled
            id_room = str(room_id)
            break
            
    rooms[id_room].append(unique_id)
    join_room(id_room)
    users[unique_id] = id_room
    
    request_boards("me")

    print(f"Connected!")
    emit('from flask', rooms, broadcast=True, json=True)
    emit('from flask', users, broadcast=True, json=True)



    #after connection to a room check if the room has the same has all the players, then emit a message to all the player in the room
@socketio.on('request_boards') #for when user need all enemy boards status again
def request_boards(message):
    recipient_session_id = users[str(request.sid)]
    if saved_board[recipient_session_id]: #when new user connected check if there are existing board
        for key, value in saved_board[recipient_session_id].items():
            emit('send_board', value, room=str(request.sid))
            print(f"{message} sending.... {value}")



@socketio.on('private_message')
def private_message(payload):
    recipient_session_id = users[str(request.sid)]#users[str(payload['username'])]
    message = payload
    emit('from flask', message, room=recipient_session_id, broadcast=False, include_self=True)

@socketio.on('player_ready')
def player_ready(message):
    recipient_session_id = users[str(request.sid)]
    message["id"] = str(request.sid)

    #if len(rooms[recipient_session_id]) != max_user_per_room:
        #room is not full, time to save the board
    
    saved_board[recipient_session_id][message["id"]] = message

    emit('send_board', message, room=recipient_session_id, broadcast=False, include_self=False) #when send board and no other player in room or room not filled
    #send everyone else my board, when everyone
        
@socketio.on('finished_process_board')
def finished_process_board(message): #this only works for 2 players
    #when player finished process board, add them to ready room
    recipient_session_id = users[str(request.sid)]
    if not str(request.sid) in ready_rooms[recipient_session_id]: #when reset board, request board again, making sure it doesnt add myself multiple times
        ready_rooms[recipient_session_id].append(str(request.sid))
    if len(ready_rooms[recipient_session_id]) == max_user_per_room:
        notfirstturn = choice(ready_rooms[recipient_session_id]) #one recieve this will set turn to false
        emit('game_ready', notfirstturn, room=recipient_session_id, broadcast=False, include_self=True) 




@socketio.on('attack')
def attack(message):
    recipient_session_id = users[str(request.sid)]
    emit('recieveAttack', message, room=recipient_session_id, broadcast=False, include_self=False)






if __name__ == '__main__':
    socketio.run(app, host='23.239.16.47', port=7777)
    #socketio.run(app, host='127.0.0.1', port=7777)