/**
 * 版权： 阳辰物联天下
 * 描述：
 * 创建时间：2018年11月19日
 */
package com.cdzg.xzshop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉redis配置〈功能详细描述〉
 *
 * @author LeBron James
 * @version [版本号, 2018年04月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    // 根据后缀查询key
    public Set<String> likeRedis(String suffix) {
        logger.info("cleanRedis prefix: {}", suffix);
        try {
            if (null != suffix) {
                if (null != redisTemplate) {
                    Set<String> keys = redisTemplate.keys("*" + suffix);
                    return keys;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("cleanRedis error : {} ", e.getMessage(), e);
            return null;
        }
    }

    // 根据后缀进行清除缓存
    public void cleanRedisSuffix(String suffix) {
        logger.info("cleanRedis prefix: {}", suffix);
        try {
            if (null != suffix) {
                if (null != redisTemplate) {
                    Set<String> keys = redisTemplate.keys("*" + suffix);
                    redisTemplate.delete(keys);
                }
            }
        } catch (Exception e) {
            logger.error("cleanRedis error : {} ", e.getMessage(), e);
        }
    }

    // 根据前缀进行清除缓存
    public void cleanRedis(String prefix) {
        logger.info("cleanRedis prefix: {}", prefix);
        try {
            if (null != prefix) {
                if (null != redisTemplate) {
                    Set<String> keys = redisTemplate.keys(prefix + "*");
                    redisTemplate.delete(keys);
                }
            }
        } catch (Exception e) {
            logger.error("cleanRedis error : {} ", e.getMessage(), e);
        }
    }

    // 根据KEY进行清除缓存
    public void cleanRedisByKey(String key) {
        logger.info("cleanRedisByKey key: {}", key);
        try {
            if (null != key) {
                if (null != redisTemplate) {
                    redisTemplate.delete(key);
                }
            }
        } catch (Exception e) {
            logger.error("cleanRedisByKey error : {} ", e.getMessage(), e);
        }
    }

    //缓存字符串
    public void putCacheStr(String key, String data, Long minus) {
        logger.info("putCacheStr : {}, {}, {} minute", key, data, minus);
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    opsValue.set(key, data);
                    redisTemplate.expire(key, minus, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error("putCacheStr error : {} ", e.getMessage(), e);
        }
    }

    // 获取缓存字符串
    public String getCacheStr(String key) {
        logger.info("getCacheStr : {}", key);
        String retStr = null;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    retStr = String.valueOf(opsValue.get(key));
                }
            }
        } catch (Exception e) {
            logger.error("getCacheStr error : {} ", e.getMessage(), e);
        }
        return retStr;
    }

    //缓存简单对象
    public void putCacheSimple(String key, Object data, Long minus) {
        logger.info("putCacheSimple : {}, {}, {} minute", key, data, minus);
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    opsValue.set(key, data);
                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheSimple error : {} ", e.getMessage(), e);
        }
    }

    //获取缓存的简单对象
    public Object getCacheSimple(String key) {
        logger.info("getCacheSimple : {}", key);
        Object object = null;
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    object = opsValue.get(key);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheSimple error : {} ", e.getMessage(), e);
        }
        return object;
    }

    //缓存List数据
    public void putCacheList(String key, List<?> datas, Long minus) {
        logger.info("putCacheList : {}, {}, {} minute", key, datas, minus);
        try {
            ListOperations<String, Object> opsList = null;
            if (null != redisTemplate) {
                opsList = redisTemplate.opsForList();
                if (null != opsList) {
                    int size = datas.size();
                    for (int i = 0; i < size; i++) {
                        opsList.leftPush(key, datas.get(i));
                    }

                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheList error : {} ", e.getMessage(), e);
        }
    }

    // 获取缓存的List对象
    public List<Object> getCacheList(String key) {
        logger.info("getCacheList : {}", key);

        List<Object> dataList = new ArrayList<Object>();
        try {
            ListOperations<String, Object> opsList = null;
            if (null != redisTemplate) {
                opsList = redisTemplate.opsForList();
                if (null != opsList) {
                    Long size = opsList.size(key);
                    for (int i = 0; i < size; i++) {
                        dataList.add(opsList.index(key, i));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getCacheList error : {} ", e.getMessage(), e);
        }
        return dataList;
    }

    //缓存SET数据
    public void putCacheSet(String key, Set<?> datas, Long minus) {
        logger.info("putCacheList : {}, {}, {} minute", key, datas, minus);
        try {
            SetOperations<String, Object> opsSet = null;
            if (null != redisTemplate) {
                opsSet = redisTemplate.opsForSet();
                if (null != opsSet) {
                    opsSet.add(key, datas);

                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheList error : {} ", e.getMessage(), e);
        }
    }

    //获取缓存的SET对象
    public Set<Object> getCacheSet(String key) {
        logger.info("getCacheSet : {}", key);
        Set<Object> dataSet = new HashSet<Object>();
        try {
            SetOperations<String, Object> opsSet = null;
            if (null != redisTemplate) {
                opsSet = redisTemplate.opsForSet();
                if (null != opsSet) {
                    dataSet = opsSet.members(key);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheSet error : {} ", e.getMessage(), e);
        }
        return dataSet;
    }

    //缓存MAP数据
    public void putCacheMap(String key, Map<Object, Object> datas, Long minus) {
        logger.info("putCacheMap : {}, {}, {} minute", key, datas, minus);
        try {
            HashOperations<String, Object, Object> opsHash = null;
            if (null != redisTemplate) {
                opsHash = redisTemplate.opsForHash();
                if (null != opsHash) {
                    opsHash.putAll(key, datas);
                    if (null != minus) {
                        redisTemplate.expire(key, minus, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("putCacheMap error : {} ", e.getMessage(), e);
        }
    }

    //获取缓存的MAP对象
    public Map<Object, Object> getCacheMap(String key) {
        logger.info("getCacheMap : {}", key);
        Map<Object, Object> dataMap = new HashMap<Object, Object>();
        try {
            HashOperations<String, Object, Object> opsHash = null;
            if (null != redisTemplate) {
                opsHash = redisTemplate.opsForHash();
                if (null != opsHash) {
                    dataMap = opsHash.entries(key);
                }
            }
        } catch (Exception e) {
            logger.error("getCacheMap error : {} ", e.getMessage(), e);
        }
        return dataMap;
    }

    //缓存简单对象永久
    public void putForeverCacheSimple(String key, Object data) {
        logger.info("putCacheSimple : {}, {}", key, data);
        try {
            ValueOperations<String, Object> opsValue = null;
            if (null != redisTemplate) {
                opsValue = redisTemplate.opsForValue();
                if (null != opsValue) {
                    opsValue.set(key, data);
                }
            }
        } catch (Exception e) {
            logger.error("putCacheSimple error : {} ", e.getMessage(), e);
        }
    }


}