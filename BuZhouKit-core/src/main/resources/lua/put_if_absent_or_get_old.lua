-- 原子性获取给定key，若key存在返回其值，若key不存在则设置key并返回null
--local key = KEYS[1]
--local value = ARGV[1]
--local expire_time_ms = ARGV[2]
--
--return redis.call('SET', key, value, 'NX', 'GET', 'PX', expire_time_ms)
local key = KEYS[1]
local value = ARGV[1]
local expire_time = tonumber(ARGV[2])

-- 尝试获取键的值
local old_value = redis.call('GET', key)
if old_value then
    return old_value -- 如果键已存在，直接返回旧值
end

-- 如果键不存在，则设置值并设置过期时间
redis.call('SET', key, value, 'PX', expire_time)
return nil -- 返回 nil 表示键之前不存在
