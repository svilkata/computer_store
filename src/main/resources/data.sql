# We should define strongly the final id and item_id for the below insert operations,
# as they are executed last and after the @PostConstruct annotated method in initSeed/AppInit.java class
USE computerstore;

select tbl.next_val from hibernate_sequences tbl where tbl.sequence_name='default' for update;
update hibernate_sequences set next_val=8  where next_val=8 and sequence_name='default';
INSERT INTO all_items (type, item_id, brand, model, buying_price, selling_price, quantity,
                       more_info,
                       photo_url,
                       size, resolution, matrix_type, view_angle, refresh_rate, brightness)
VALUES ('monitor', 9, 'Dell', 'Dell P2422H - P2422H', 600.25, 650.55, 8,
        'Интерфейси: USB Type-C с DisplayPort ф-я, DisplayPort цифров изход, Ethernet (RJ-45), 1 x HDMI 1.4, 1 x DisplayPort 1.2, 4 x USB 3.2 Downstream',
        'https://res.picture.com/dtfd8gw16/image/upload/v1658772656/computerStore/monitors/z24nddjazcimyal5kpf5.jpg',
        '23.8" (60.45 cm)', '1920 x 1080', 'IPS', '178/178', '60 Hz', '250 cd/m2');


select tbl.next_val from hibernate_sequences tbl where tbl.sequence_name='default' for update;
update hibernate_sequences set next_val=8  where next_val=8 and sequence_name='default';
INSERT INTO all_items (type, item_id, brand, model, buying_price, selling_price, quantity,
                       more_info,
                       photo_url,
                       size, resolution, matrix_type, view_angle, refresh_rate, brightness)
VALUES       ('monitor', 10, 'Philips', 'Philips 243V7QDSB', 250.25, 330.22, 9,
        'Интерфейси: VGA, 1 x HDMI, 1 x DVI-D порт, 1 x Audio Out',
        'https://res.picture.com/dtfd8gw16/image/upload/v1658773066/computerStore/monitors/vw7m6tpotdp4yo0pqcun.jpg',
        '23.8" (60.5 cm)', '1920 x 1080', 'IPS', '178/178', '75 Hz', '250 cd/m2');


INSERT INTO pictures (id, item_id, public_id, url)
VALUES (9, 9, 'z24nddjazcimyal5kpf5',
        'https://res.picture.com/dtfd8gw16/image/upload/v1658772656/computerStore/monitors/z24nddjazcimyal5kpf5.jpg'),
       (10, 10, 'vw7m6tpotdp4yo0pqcun',
        'https://res.picture.com/dtfd8gw16/image/upload/v1658773066/computerStore/monitors/vw7m6tpotdp4yo0pqcun.jpg');

