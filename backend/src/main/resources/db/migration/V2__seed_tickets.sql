-- 20 tickets distribuídos nos últimos 7 dias.
-- Distribuição: status (8 OPEN, 5 IN_PROGRESS, 7 CLOSED),
-- priority (5 HIGH, 8 MEDIUM, 7 LOW), 7 categorias.

INSERT INTO tickets
    (id, customer_name, channel, subject, description, category, status, priority, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'Alice Souza',        'EMAIL',    'Cobrança duplicada em abril',       'Fatura veio com dois débitos iguais.',      'billing',         'OPEN',        'HIGH',   now() - interval '6 days', now() - interval '6 days'),
    (gen_random_uuid(), 'Bruno Carvalho',     'WHATSAPP', 'Problema ao fazer login',           'Erro de credenciais mesmo com senha certa.', 'support',        'IN_PROGRESS', 'MEDIUM', now() - interval '6 days', now() - interval '6 days'),

    (gen_random_uuid(), 'Carla Dias',         'EMAIL',    'Boleto não recebido',               NULL,                                         'billing',         'CLOSED',      'LOW',    now() - interval '5 days', now() - interval '5 days'),
    (gen_random_uuid(), 'Diego Lima',         'CHAT',     'Tela de checkout trava',            'Ao clicar em finalizar a página congela.',   'bug',            'OPEN',        'HIGH',   now() - interval '5 days', now() - interval '5 days'),
    (gen_random_uuid(), 'Eduarda Moreira',    'PHONE',    'Atendimento demorado',              NULL,                                         'complaint',       'CLOSED',      'MEDIUM', now() - interval '5 days', now() - interval '5 days'),

    (gen_random_uuid(), 'Felipe Nogueira',    'EMAIL',    'Exportar relatório em PDF',         'Gostaria de baixar o dashboard em PDF.',     'feature-request', 'OPEN',        'LOW',    now() - interval '4 days', now() - interval '4 days'),
    (gen_random_uuid(), 'Gabriela Rocha',     'WHATSAPP', 'Como alterar dados cadastrais',     NULL,                                         'support',         'CLOSED',      'LOW',    now() - interval '4 days', now() - interval '4 days'),

    (gen_random_uuid(), 'Henrique Silva',     'CHAT',     'Erro 500 ao salvar perfil',         'Stack trace no console do navegador.',       'bug',             'IN_PROGRESS', 'HIGH',   now() - interval '3 days', now() - interval '3 days'),
    (gen_random_uuid(), 'Isabela Teixeira',   'EMAIL',    'Ajuste na fatura de março',         NULL,                                         'billing',         'OPEN',        'MEDIUM', now() - interval '3 days', now() - interval '3 days'),
    (gen_random_uuid(), 'João Vasconcelos',   'PHONE',    'Solicitação de cancelamento',       'Motivo: mudança de fornecedor.',             'cancellation',    'IN_PROGRESS', 'MEDIUM', now() - interval '3 days', now() - interval '3 days'),
    (gen_random_uuid(), 'Karina Xavier',      'CHAT',     'Não consigo enviar mensagens',      NULL,                                         'support',         'OPEN',        'MEDIUM', now() - interval '3 days', now() - interval '3 days'),

    (gen_random_uuid(), 'Leandro Yamada',     'WHATSAPP', 'Integração com Zapier',             'Queremos automatizar criação de tickets.',   'feature-request', 'OPEN',        'LOW',    now() - interval '2 days', now() - interval '2 days'),
    (gen_random_uuid(), 'Mariana Zago',       'EMAIL',    'Reembolso pendente',                NULL,                                         'billing',         'CLOSED',      'MEDIUM', now() - interval '2 days', now() - interval '2 days'),
    (gen_random_uuid(), 'Nicolas Alencar',    'CHAT',     'Anexos não abrem',                  'Links de download retornam 404.',            'bug',             'OPEN',        'HIGH',   now() - interval '2 days', now() - interval '2 days'),

    (gen_random_uuid(), 'Olivia Barros',      'EMAIL',    'Onboarding incompleto',             'Alguns passos ficaram pela metade.',         'support',         'IN_PROGRESS', 'MEDIUM', now() - interval '1 days', now() - interval '1 days'),
    (gen_random_uuid(), 'Pedro Camargo',      'WHATSAPP', 'Produto chegou danificado',         NULL,                                         'complaint',       'OPEN',        'MEDIUM', now() - interval '1 days', now() - interval '1 days'),
    (gen_random_uuid(), 'Renata Duarte',      'PHONE',    'Questionamento sobre juros',        NULL,                                         'billing',         'CLOSED',      'LOW',    now() - interval '1 days', now() - interval '1 days'),

    (gen_random_uuid(), 'Sérgio Esteves',     'CHAT',     'Dark mode no dashboard',            'Tema escuro para uso noturno.',              'feature-request', 'CLOSED',      'LOW',    now(),                     now()),
    (gen_random_uuid(), 'Tatiana Freitas',    'EMAIL',    'Filtro de busca não funciona',      'Campo de busca ignora acentuação.',          'bug',             'IN_PROGRESS', 'HIGH',   now(),                     now()),
    (gen_random_uuid(), 'Ulisses Galvão',     'WHATSAPP', 'Primeira configuração',             NULL,                                         'onboarding',      'CLOSED',      'LOW',    now(),                     now());
