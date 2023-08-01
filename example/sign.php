<?php


$url = 'http://vip.123pan.com/29/音乐/02.一千零一夜-李克勤.wma';

$private_key = '289ds32418bxdba';

$uid = 29;

$expire_time = time() + 30;   // 该签发的资源30s以后过期

$rand_value = rand(0, 100000); // 生成随机数

$parse_result = parse_url($url); // 解析 URL

$request_path = $parse_result["path"]; // /29/音乐/02.一千零一夜-李克勤.wma

$sign = md5(sprintf("%s-%d-%d-%d-%s", $request_path, $expire_time, $rand_value, $uid, $private_key));

$wait = sprintf("%d-%d-%d-%s", $expire_time, $rand_value, $uid, $sign);

$result = $url . "?auth_key=" . $wait;

echo $result;
