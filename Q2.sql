create table content(
    id serial primary key not null,
    name text,
    content_type varchar(30),
    description text,
    watched integer,
    others jsonb not null default '{}',
    created_at timestamp not null,
    updated_at timestamp not null
);

create table users
{
    id serial primary key not null,
    name text,
    email text,
    password text,
    created_at timestamp not null,
    updated_at timestamp not null

}

create table user_subscriptions
{
    id serial primary key not null,
    userId integer not null,
    subscriptionId integer not null,
    subscriptionStartDate date,
    subscriptionEndDate date,
    status varchar(10),
    created_at timestamp not null,
    updated_at timestamp not null
}

create table subscriptions
{
    id serial primary key not null,
    offer_name text,
    offer_duration integer,
    offer_price float not null,
    offer_details jsonb not null default '{}'
}

CREATE INDEX IF NOT EXISTS index_on_content_name ON content(name);
CREATE INDEX IF NOT EXISTS index_on_users_email ON users(email);