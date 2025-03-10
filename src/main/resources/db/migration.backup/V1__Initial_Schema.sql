-- Users table
create table users (
    id uuid primary key,
    username varchar(50) not null unique,
    email varchar(100) not null unique,
    password_hash varchar(255) not null,
    display_name varchar(100),
    bio text,
    location varchar(100),
    website varchar(255),
    profile_image varchar(255),
    header_image varchar(255),
    verified boolean not null default false,
    enabled boolean not null default true,
    email_verified boolean not null default false,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
comment on table users is 'Table storing all registered users in the system';

-- User roles
create table user_roles (
    user_id uuid not null references users(id) on delete cascade,
    role varchar(20) not null,
    primary key (user_id, role)
);
comment on table user_roles is 'Roles assigned to each user for authorization';

-- Posts (tweets) table
create table posts (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    content text not null,
    is_reply boolean not null default false,
    parent_id uuid references posts(id) on delete set null,
    is_repost boolean not null default false,
    original_post_id uuid references posts(id) on delete set null,
    like_count int not null default 0,
    reply_count int not null default 0,
    repost_count int not null default 0,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
comment on table posts is 'Table storing all posts (tweets) created by users';

-- Post media attachments
create table post_media (
    post_id uuid not null references posts(id) on delete cascade,
    media_url varchar(255) not null,
    primary key (post_id, media_url)
);
comment on table post_media is 'Media attachments (images, etc.) for posts';

-- Hashtags table
create table hashtags (
    id uuid primary key,
    name varchar(50) not null unique,
    post_count int not null default 0,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
comment on table hashtags is 'Hashtags used in posts for categorization and searching';

-- Mapping between posts and hashtags
create table post_hashtags (
    post_id uuid not null references posts(id) on delete cascade,
    hashtag_id uuid not null references hashtags(id) on delete cascade,
    primary key (post_id, hashtag_id)
);
comment on table post_hashtags is 'Many-to-many mapping between posts and hashtags';

-- Likes table
create table likes (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    post_id uuid not null references posts(id) on delete cascade,
    created_at timestamp not null default now(),
    unique (user_id, post_id)
);
comment on table likes is 'Likes given by users to posts';

-- Follows table - relationship between users
create table follows (
    id uuid primary key,
    follower_id uuid not null references users(id) on delete cascade,
    following_id uuid not null references users(id) on delete cascade,
    created_at timestamp not null default now(),
    unique (follower_id, following_id)
);
comment on table follows is 'Follow relationships between users';

-- Notifications table
create table notifications (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    type varchar(20) not null,
    actor_id uuid not null references users(id) on delete cascade,
    post_id uuid references posts(id) on delete set null,
    read boolean not null default false,
    created_at timestamp not null default now()
);
comment on table notifications is 'Notifications for user activities';

-- Direct Messages - Conversations
create table dm_conversations (
    id uuid primary key,
    name varchar(100),
    is_group boolean not null default false,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
comment on table dm_conversations is 'Direct messaging conversations';

-- Direct Messages - Conversation Participants
create table dm_participants (
    conversation_id uuid not null references dm_conversations(id) on delete cascade,
    user_id uuid not null references users(id) on delete cascade,
    joined_at timestamp not null default now(),
    primary key (conversation_id, user_id)
);
comment on table dm_participants is 'Participants in direct messaging conversations';

-- Direct Messages - Messages
create table dm_messages (
    id uuid primary key,
    conversation_id uuid not null references dm_conversations(id) on delete cascade,
    sender_id uuid not null references users(id) on delete cascade,
    content text not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
comment on table dm_messages is 'Messages in direct messaging conversations';

-- Direct Messages - Message Status (read/delivered)
create table dm_message_status (
    message_id uuid not null references dm_messages(id) on delete cascade,
    user_id uuid not null references users(id) on delete cascade,
    is_read boolean not null default false,
    read_at timestamp,
    primary key (message_id, user_id)
);
comment on table dm_message_status is 'Status of messages (read/unread) for participants';

-- Bookmarks
create table bookmarks (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    post_id uuid not null references posts(id) on delete cascade,
    created_at timestamp not null default now(),
    unique (user_id, post_id)
);
comment on table bookmarks is 'Posts bookmarked by users';

-- Indexes for performance
create index idx_posts_user_id on posts(user_id);
create index idx_posts_parent_id on posts(parent_id);
create index idx_posts_created_at on posts(created_at);
create index idx_posts_original_post_id on posts(original_post_id);
create index idx_likes_post_id on likes(post_id);
create index idx_likes_user_id on likes(user_id);
create index idx_follows_follower_id on follows(follower_id);
create index idx_follows_following_id on follows(following_id);
create index idx_notifications_user_id on notifications(user_id);
create index idx_notifications_created_at on notifications(created_at);
create index idx_bookmarks_user_id on bookmarks(user_id);
create index idx_hashtags_name on hashtags(name);
create index idx_hashtags_post_count on hashtags(post_count desc);
create index idx_dm_messages_conversation_id on dm_messages(conversation_id);
create index idx_dm_messages_created_at on dm_messages(created_at);
create index idx_dm_participants_user_id on dm_participants(user_id); 