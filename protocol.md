Client <-> Server

# Both the server and client can initiate communication. 
# A message can either be a reply, or a call (call = request)

    call replyNumber command parameters {data}
    reply replyNumber response


# Client commands:

    pm command:
    call replyNumber pm receiver {message}

    login command:
    call replyNumber login username password

