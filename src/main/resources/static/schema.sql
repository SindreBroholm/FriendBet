create schema FriendBet;

create table user(
    id int AUTO_INCREMENT UNIQUE,
    name varchar(150) not null,
    username varchar(150) not null unique,
    password varchar(300) not null,
    confirm_password varchar(300) not null,
    role varchar(100) not null,
    credit varchar(10000) not null,
    primary key (id)
);

create table bet(
    id int AUTO_INCREMENT unique not null,
    name varchar(150) not null,
    odds_name varchar(100),
    odds varchar(100) not null,
    bet_type enum('OneVsOne', 'OneVsMany', 'ManyVsMany'),
    description varchar(5000),
    primary key (id)
);

create table community(
    id int AUTO_INCREMENT UNIQUE NOT NULL,
    name varchar(150) not null,
    primary key (id)
);

create table bet_collector(
    id int AUTO_INCREMENT UNIQUE NOT NULL,
    created datetime,
    user_id int not null,
    bet_id int not null,
    primary key (id),
    foreign key (user_id) references user(id),
    foreign key (bet_id) references bet(id)
);

create table friend_tracker(
    id int AUTO_INCREMENT UNIQUE NOT NULL,
    user_id int not null,
    friend_id int not null,
    pending boolean,
    primary key (id),
    foreign key (user_id) references user(id),
    foreign key (friend_id) references user(id)
);

create table chat_room(
    id int auto_increment unique not null,
    chat_id varchar(10) unique not null,
    sender_id int,
    recipient_id int,
    primary key (id),
    foreign key (sender_id) references user(id),
    foreign key (recipient_id) references user(id)
);

create table Chat_message(

    id int auto_increment unique not null,
    chat_id varchar(10),
    sender_id int,
    recipient_id int,
    sender_name varchar(150),
    recipient_name varchar(150),
    content varchar(5000),
    date datetime,
    primary key (id),
    foreign key (chat_id) references chat_room(chat_id),
    foreign key (sender_id) references user(id),
    foreign key (recipient_id) references user(id)
);

