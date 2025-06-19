import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import type { DayStats } from '../types';

export const useTaskStore = defineStore('tasks', () => {
  const dayStats = ref<DayStats[]>([]);

  // Add this debug function
  const debugDayStats = computed(() => {
    console.log('Day stats structure:', dayStats.value?.map((stat: DayStats) => ({
      date: stat.date,
      completed: stat.completed,
      uncompleted: stat.uncompleted,
      total: stat.total
    })));
    return dayStats.value;
  });

  return {
    dayStats,
    debugDayStats,
    // ... rest of your existing exports ...
  };
}); 