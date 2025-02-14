insert into "github_ai_assistant"."workspace" ("id", "name", "description")
values (cast('aa9664a9-794c-462d-8f65-17326e02355c' as uuid), 'My fancy workspace', 'My fancy description. Very cool!');

insert into "github_ai_assistant"."github_repository" ("id", "full_name", "url", "workspace_id")
values (cast('3051ac01-de50-4932-95d9-021faa0f7023' as uuid), 'some-coder/my-fancy-repo',
        'https://github.com/some-coder/my-fancy-repo', cast('aa9664a9-794c-462d-8f65-17326e02355c' as uuid));

insert into "github_ai_assistant"."llm_conversation" ("id", "repo_id", "issue_id", "created_at")
values (cast('b577b017-a9ef-4c6a-b888-fbc0ca89e959' as uuid), cast('3051ac01-de50-4932-95d9-021faa0f7023' as uuid), 419,
        timestamp with time zone '2025-02-14 22:15:59.517149+00:00');

insert into "github_ai_assistant"."llm_message" ("id", "role", "content", "created_at")
values (cast('f8955653-d40f-4625-ae3a-f74871c1a596' as uuid), cast('USER' as "github_ai_assistant"."llm_message_role"),
        'This is my first message!', timestamp with time zone '2025-02-14 22:15:59.539899+00:00');

insert into "github_ai_assistant"."llm_message" ("id", "role", "content", "created_at")
values (cast('a53b3154-2d70-4831-9e41-eb8d4c4eaa83' as uuid), cast('USER' as "github_ai_assistant"."llm_message_role"),
        'This is my second one!', timestamp with time zone '2025-02-14 22:15:59.54569+00:00');

insert into "github_ai_assistant"."llm_message" ("id", "role", "content", "created_at")
values (cast('62781c3f-4654-4e6d-b5af-d91d70a68ec8' as uuid), cast('USER' as "github_ai_assistant"."llm_message_role"),
        'This is the third!', timestamp with time zone '2025-02-14 22:15:59.548562+00:00');

insert into "github_ai_assistant"."conversation_message" ("conversation_id", "message_id")
values (cast('b577b017-a9ef-4c6a-b888-fbc0ca89e959' as uuid), cast('f8955653-d40f-4625-ae3a-f74871c1a596' as uuid)),
       (cast('b577b017-a9ef-4c6a-b888-fbc0ca89e959' as uuid), cast('a53b3154-2d70-4831-9e41-eb8d4c4eaa83' as uuid)),
       (cast('b577b017-a9ef-4c6a-b888-fbc0ca89e959' as uuid), cast('62781c3f-4654-4e6d-b5af-d91d70a68ec8' as uuid));