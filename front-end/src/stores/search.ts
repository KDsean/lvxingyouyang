import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { HotelSearchParams } from '@/types'

export const useSearchStore = defineStore('search', () => {
  // 酒店搜索参数
  const hotelSearchParams = ref<HotelSearchParams>({
    page: 1,
    pageSize: 20
  })
  
  // 机票搜索参数
  const flightSearchParams = ref({
    from: '',
    to: '',
    date: '',
    returnDate: '',
    passengers: 1,
    cabinClass: 'economy'
  })
  
  // 火车票搜索参数
  const trainSearchParams = ref({
    from: '',
    to: '',
    date: ''
  })
  
  // 租车搜索参数
  const carSearchParams = ref({
    location: '',
    pickupDate: '',
    returnDate: ''
  })
  
  // 更新酒店搜索参数
  const updateHotelSearch = (params: Partial<HotelSearchParams>) => {
    hotelSearchParams.value = { ...hotelSearchParams.value, ...params }
  }
  
  // 更新机票搜索参数
  const updateFlightSearch = (params: any) => {
    flightSearchParams.value = { ...flightSearchParams.value, ...params }
  }
  
  // 更新火车票搜索参数
  const updateTrainSearch = (params: any) => {
    trainSearchParams.value = { ...trainSearchParams.value, ...params }
  }
  
  // 更新租车搜索参数
  const updateCarSearch = (params: any) => {
    carSearchParams.value = { ...carSearchParams.value, ...params }
  }
  
  return {
    hotelSearchParams,
    flightSearchParams,
    trainSearchParams,
    carSearchParams,
    updateHotelSearch,
    updateFlightSearch,
    updateTrainSearch,
    updateCarSearch
  }
})
