<?php

$hwId = $argv[1];
$token = $argv[2];
$appId = $argv[3];

function pwCall($method, $data) {
    $url = 'https://cp.pushwoosh.com/json/1.3/' . $method;
    $request = json_encode(['request' => $data]);

    echo ($request);

    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);
    curl_setopt($ch, CURLOPT_ENCODING, 'gzip, deflate');
    curl_setopt($ch, CURLOPT_HEADER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $request);

    $response = curl_exec($ch);
    $info = curl_getinfo($ch);
    curl_close($ch);

    print "[PW] request: $request\n";
    print "[PW] response: $response\n";
    print "[PW] info: " . print_r($info, true);
}

$messages = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
$notifications = [];
foreach ($messages as $message) {
    $notifications[] = [
                'ios_badges' => '+1',
                'send_date' => 'now',
                'content' => 'Iain test - ' . $message,
                'android_custom_icon' => 'https://www.pushwoosh.com/wp-content/themes/pushwoosh/img/ico-big-features.png',
                'data' => [
                    'push_message_id' => $message
                ],
                'devices' => [
                    $hwId
                ]
            ];
}

pwCall('createMessage', [
    'application' => $appId,
    'auth' => $token,
    'notifications' => $notifications
    ]
);
